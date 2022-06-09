package club.cred.neopop.tiltButton

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.forEach
import club.cred.neopop.TiltFrameLayout
import club.cred.neopop.common.PopButtonAnimationListener
import club.cred.neopop.common.areSystemAnimationsEnabled
import club.cred.neopop.common.isEventWithinBounds
import club.cred.neopop.common.provideSafeHapticFeedback
import club.cred.neopop.common.resumeOrStart

class TiltAnimationHelper(private val tiltLayout: TiltFrameLayout) {

    // ///////////////////////////////////////////
    // Variables
    // //////////////////////////////////////////

    private var state = TiltAnimationState()
    private var previousAnimationValue: Int = 0
    private var shouldPerformClick = false
    private val popButtonAnimationListeners = ArrayList<PopButtonAnimationListener>()
    private val isFloating: Boolean
        get() = tiltLayout.gravity == NeoPopGravity.ON_SPACE

    private val animator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = AccelerateDecelerateInterpolator()
        }
    }
    private val updateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            if (!tiltLayout.context.areSystemAnimationsEnabled() && state.currentState !is TiltAnimationState.State.Floating) {
                onAnimationUpdate(it.animatedValue as Float)
            } else if (tiltLayout.context.areSystemAnimationsEnabled()) {
                onAnimationUpdate(it.animatedValue as Float)
            }
        }
    }
    private val endListener: (animator: Animator) -> Unit = {
        if (tiltLayout.isEnabled) {
            state.currentState = TiltAnimationState.State.Floating
            state.setNextType(isFloating)
            animator.updateAnimDuration()
            animate(false)
        }
    }

    /** Used to pause the button on top before restarting floating animation */
    private var pendingDelay: Long = -1

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    fun startAnimation() {
        if (isFloating) {
            animate(false)
        }
    }

    private fun animate(
        immediate: Boolean = false,
    ) {
        if (tiltLayout.animateOnTouch || isFloating) {
            animator.removeAllListeners()
            animator.removeAllUpdateListeners()

            animator.updateAnimDuration()
            animator.addUpdateListener(updateListener)

            val motionEventDown =
                state.isButtonGoingDown && state.currentState is TiltAnimationState.State.Touch

            if (isFloating) {

                if (!motionEventDown) {
                    animator.doOnEnd(endListener)
                }

                if (state.isButtonGoingDown && pendingDelay > 0) {
                    // button is at top and about to go down
                    animator.startDelay = pendingDelay
                    pendingDelay = -1
                } else {
                    animator.startDelay = 0L
                }
            }

            if (!immediate) {
                if (motionEventDown) animator.resumeOrStart() else animator.start()
            } else {
                animator.end()
            }
        } else {
            onAnimationUpdate(1f)
        }
    }

    private fun onAnimationUpdate(progress: Float) {
        state.currentProgress = progress

        val value = if (state.isButtonGoingUp) 1 - progress else progress
        val animatedValue = value * tiltLayout.depth
        val blackShadowTranslationLength =
            tiltLayout.shadowHeight -
                tiltLayout.shadowTopMargin -
                tiltLayout.depth

        val blackShadowAnimatedValue = value * blackShadowTranslationLength

        if (isFloating) {
            tiltLayout.animateShadow(blackShadowAnimatedValue)
        }

        tiltLayout.onAnimate(0f, animatedValue)
        tiltLayout.animatePadding(animatedValue.toInt())
    }

    fun onTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                shouldPerformClick = true
                doMotionEventDownAnimation()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_MOVE -> {
                if (!shouldPerformClick) {
                    return true
                }

                // if finger moves out of bounds, reset button
                if (!tiltLayout.isEventWithinBounds(event)) {
                    shouldPerformClick = false
                    doMotionEventUpAnimation()
                } else if (event.action != MotionEvent.ACTION_MOVE) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        tiltLayout.performClick()
                    }
                    doMotionEventUpAnimation(ANIMATION_START_DELAY)
                }
                return true
            }
        }
        return false
    }

    private fun doMotionEventDownAnimation() {
        if (animator.isRunning) {
            animator.pause()
        }
        state.isButtonGoingDown = true
        state.currentState = TiltAnimationState.State.Touch
        animate(false)
        tiltLayout.provideSafeHapticFeedback()
    }

    private fun doMotionEventUpAnimation(delay: Long = -1) {
        if (tiltLayout.isEnabled && !state.isButtonAtTop) {
            state.isButtonGoingUp = true
            state.currentState = TiltAnimationState.State.Touch
            pendingDelay = delay
            animate(false)
        }
    }

    fun enableAnimation(shouldAnimate: Boolean = true) {
        if (tiltLayout.isEnabled) {
            if (isFloating) {
                // if it's a floating button, enabling should move it up
                state.isButtonGoingUp = true
                state.currentState = TiltAnimationState.State.Enabling
                animate(immediate = !shouldAnimate)
            }
        } else {
            if (isFloating && !state.isButtonAtBottom) {

                // if it's a floating button, disabling should move it down
                state.isButtonGoingUp = false
                state.currentState = TiltAnimationState.State.Enabling
                animate(immediate = !shouldAnimate)
            } else if (!isFloating && !state.isButtonAtTop) {

                // if it's a non floating button, disabling should move it up
                // note: technically for non floating, the button doesn't move
                // but in case it's in a semi pressed state and button is disabled
                // we need to reset it to top position
                state.isButtonGoingUp = true
                state.currentState = TiltAnimationState.State.Enabling
                animate(immediate = !shouldAnimate)
            }
        }
    }

    private fun View.animatePadding(animatedValue: Int) {
        val delta = animatedValue - previousAnimationValue
        if (this is TextView) {
            setPadding(
                paddingLeft,
                paddingTop + delta,
                paddingRight,
                paddingBottom - delta
            )
        } else if (this is ViewGroup) {
            this.forEach {
                it.translationY += delta
            }
        }
        previousAnimationValue = animatedValue
    }

    private fun ValueAnimator.updateAnimDuration() {
        this.duration = getAnimationDuration()
    }

    private fun getAnimationDuration(): Long =
        when (state.currentState) {
            TiltAnimationState.State.Enabling -> if (tiltLayout.animateOnTouch) 0 else TILT_PRESS_DURATION
            TiltAnimationState.State.Floating -> TILT_FLOATING_DURATION
            TiltAnimationState.State.Touch -> if (tiltLayout.animateOnTouch) 0 else TILT_PRESS_DURATION
        }

    fun clear() {
        animator.removeAllUpdateListeners()
        popButtonAnimationListeners.clear()
        animator.removeAllListeners()
        animator.cancel()
    }

    fun addPopButtonAnimationListener(listener: PopButtonAnimationListener) {
        popButtonAnimationListeners.add(listener)
    }

    fun removePopButtonAnimationListener(listener: PopButtonAnimationListener) {
        if (popButtonAnimationListeners.contains(listener)) {
            popButtonAnimationListeners.remove(listener)
        }
    }

    companion object {
        const val TILT_PRESS_DURATION = 50L
        const val TILT_FLOATING_DURATION = 2000L
        const val ANIMATION_START_DELAY = 5000L
    }
}
