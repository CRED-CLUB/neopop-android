package club.cred.neopop.common

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd

class ShimmerAnimationHelper(
    private val duration: Long,
    private val neoPopDrawable: NeoButtonDrawableInteractor,
) {

    internal var isShimmerAnimating = false
        private set

    private var onEnd: (() -> Unit)? = null
    private val linearInterpolator = LinearInterpolator()
    private val shimmerAnimator: ValueAnimator by lazy { ValueAnimator.ofFloat(0f, 1f) }
    internal var width: Int by onFieldChange(0) { startShimmer() }
    internal var repeatDelay: Long by onFieldChange(0L) { startShimmer() }
    internal var startDelay: Long by onFieldChange(0L) { startShimmer() }

    var isDrawableEnabled: Boolean by onFieldChange(true) {
        if (it) {
            startShimmer()
        } else {
            stopShimmer(true)
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    fun startShimmer() {
        if (!isShimmerAnimating && width > 0 && isDrawableEnabled) {
            shimmerAnimator.apply {
                removeListeners()
                startDelay = this@ShimmerAnimationHelper.startDelay
                duration = this@ShimmerAnimationHelper.duration
                interpolator = linearInterpolator
                addUpdateListener {
                    val animatedValue = (this.animatedValue as Float) * width
                    neoPopDrawable.onShimmerAnimation(
                        animatedValue,
                        animatedValue / width
                    )
                }
                doOnEnd {
                    if (isShimmerAnimating) {
                        startDelay = repeatDelay
                        start()
                    } else {
                        stopImmediately(onEnd)
                    }
                }
                start()
                isShimmerAnimating = true
            }
        }
    }

    fun stopShimmer(stopImmediate: Boolean = false, onEnd: (() -> Unit)? = null) {
        if (isShimmerAnimating) {
            if (stopImmediate) {
                stopImmediately(onEnd)
            } else {
                // store and fire later when anim is done
                this.onEnd = onEnd
            }
            isShimmerAnimating = false
        }
    }

    private fun stopImmediately(onEnd: (() -> Unit)?) {
        removeListeners()
        shimmerAnimator.cancel()
        onEnd?.invoke()
        this.onEnd = null // single shot so null it
    }

    private fun removeListeners() {
        shimmerAnimator.removeAllUpdateListeners()
        shimmerAnimator.removeAllListeners()
    }
}
