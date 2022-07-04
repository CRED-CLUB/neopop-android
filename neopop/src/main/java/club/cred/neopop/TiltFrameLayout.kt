/*
 *
 *  * Copyright 2022 Dreamplug Technologies Private Limited
 *  * Licensed under the Apache License, Version 2.0 (the “License”);
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package club.cred.neopop

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import club.cred.neopop.common.dp
import club.cred.neopop.common.dynamicAttr
import club.cred.neopop.common.getMovePath
import club.cred.neopop.common.withAttrs
import club.cred.neopop.popButton.NeoPopGeometry
import club.cred.neopop.popButton.PopFrameLayoutStyle
import club.cred.neopop.tiltButton.NeoPopGravity
import club.cred.neopop.tiltButton.TiltAnimationHelper
import club.cred.neopop.tiltButton.TiltDrawable
import club.cred.neopop.tiltButton.TiltFrameLayoutStyle
import club.cred.neopop.tiltButton.TiltFrameLayoutStyle.Companion.getDisabledStateStyle
import club.cred.neopop.tiltButton.TiltGeometry
import club.cred.neopop.tiltButton.TiltGeometry.Companion.DEFAULT_BOTTOM_SURFACE_ROTATION
import club.cred.neopop.tiltButton.TiltGeometry.Companion.DEFAULT_CENTER_SURFACE_ROTATION
import club.cred.neopop.tiltButton.TiltGeometry.Companion.DEFAULT_SHADOW_WIDTH
import club.cred.neopop.tiltButton.TiltGeometry.Companion.DEFAULT_SURFACE_COLOR
import com.dreamplug.neopop.R
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class TiltFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * true to enable shimmer
     */
    var showShimmer: Boolean by onFieldChange(true) { copy(hasShimmer = it) }

    /**
     * bottom surface color
     */
    var bottomSurfaceColor: Int by onFieldChange(DEFAULT_SURFACE_COLOR) { copy(bottomSurfaceColor = it) }

    /**
     * set true to enable strokes
     */
    var isStrokeEnabled: Boolean by onFieldChange(false) { copy(isStrokeEnabled = it) }

    /**
     * bottom surface shimmer color
     */
    var bottomShimmerColor by onFieldChange(TiltGeometry.SHIMMER_COLOR) { copy(bottomShimmerColor = it) }
    var shimmerStartDelay: Long by onFieldChange(0L) { copy(shimmerStartDelay = it) }
    var shimmerRepeatDelay: Long by onFieldChange(0L) { copy(shimmerRepeatDelay = it) }
    var strokeWidth: Float by onFieldChange(NeoPopGeometry.DEFAULT_STROKE_WIDTH) { copy(strokeWidth = it) }
    var shimmerColor: Int by onFieldChange(TiltGeometry.SHIMMER_COLOR) { copy(shimmerColor = it) }
    var strokeColor: Int by onFieldChange(TiltGeometry.STROKE_COLOR) { copy(strokeColor = it) }
    var depth: Float by onFieldChange(DEFAULT_SHADOW_WIDTH) { copy(depth = it) }
    var shadowColor: Int by onFieldChange(TiltGeometry.DEFAULT_SHADOW_COLOR) {
        copy(shadowColor = it)
    }

    val isShimmerAnimationRunning: Boolean
        get() = popDrawable.isShimmerAnimating

    // /////////////////////////////////////////////////////////////////////////
    // Internal variables
    // /////////////////////////////////////////////////////////////////////////

    private var centerSurfaceRotation: Float = DEFAULT_CENTER_SURFACE_ROTATION
    private var bottomSurfaceRotation: Float = DEFAULT_BOTTOM_SURFACE_ROTATION
    private var shimmerWidth: Float = 0.dp
    private var shimmerDuration: Int = 0

    private val popDrawable: TiltDrawable = TiltDrawable()
    private var onLayoutCalled = false

    /**
     padding between shadow and button while floating
     **/
    internal var shadowTopMargin = 0.dp
    internal var shadowHeight = 0.dp
    internal var gravity: NeoPopGravity = NeoPopGravity.ON_SPACE
    internal var animateOnTouch: Boolean by onFieldChange(TiltGeometry.ANIMATE_ON_TOUCH) {
        copy(animateOnTouch = it)
    }
    private var centerSurfaceColor: Int by onFieldChange(0) {
        copy(
            centerSurfaceColor = it,
            bottomSurfaceColor = PopFrameLayoutStyle.getVerticalSurfaceColor(it)
        )
    }

    internal var tiltStyle: TiltFrameLayoutStyle by dynamicAttr(TiltFrameLayoutStyle()) {
        disabledStyle = getDisabledStateStyle(tiltStyle)
        popDrawable.tiltStyle = if (isEnabled) it else disabledStyle
        updateBackground()
    }

    private var disabledStyle: TiltFrameLayoutStyle by dynamicAttr(
        getDisabledStateStyle(tiltStyle)
    ) {
        popDrawable.tiltStyle = if (isEnabled) tiltStyle else disabledStyle
    }

    private val animationHelper: TiltAnimationHelper by lazy {
        TiltAnimationHelper(this)
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    init {
        parseAttrsAndCreateStyle(attrs)
        setPadding()
        animationHelper.startAnimation()
        if (showShimmer) {
            startShimmer()
        }
    }

    /**
     * Start shimmer animation if shimmer is enabled
     */
    fun startShimmer() {
        popDrawable.startShimmer()
    }

    /**
     * Stop shimmer animation if shimmer animation is running
     * @param stopImmediate - true, if shimmer should stop immediately without completing the sweep
     * @param onEnd - If [stopImmediate] is false, this lambda can be used to notify when shimmer
     * has completed it's sweep
     */
    fun stopShimmer(stopImmediate: Boolean = false, onEnd: (() -> Unit)? = null) {
        popDrawable.stopShimmer(stopImmediate, onEnd)
    }

    /**
     * Same as [setEnabled] with the option to animate between disabled
     * and enabled states using [shouldAnimate]
     */
    fun setEnabled(enabled: Boolean, shouldAnimate: Boolean = true) {
        val isUpdated = enabled != isEnabled
        super.setEnabled(enabled)
        if (isUpdated) {
            animationHelper.enableAnimation(shouldAnimate)
            popDrawable.tiltStyle =
                if (enabled) tiltStyle else disabledStyle
        }
    }

    /**
     * Set a disabled style for this button when disabling it
     */
    fun setDisableStyle(
        cardColor: Int,
        shadowColor: Int = PopFrameLayoutStyle.getVerticalSurfaceColor(cardColor)
    ) {
        disabledStyle = TiltFrameLayoutStyle.createDisableStyle(
            tiltStyle,
            cardColor,
            shadowColor
        )
    }

    /**
     * Reset disabled style to default disabled style
     */
    fun resetDisableState() {
        disabledStyle = getDisabledStateStyle(tiltStyle)
    }

    // /////////////////////////////////////////////////////////////////////////
    // Internal and Overridden methods
    // /////////////////////////////////////////////////////////////////////////

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animationHelper.startAnimation()
        if (showShimmer) {
            startShimmer()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animationHelper.clear()
        stopShimmer(stopImmediate = true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (!isEnabled || !isClickable) {
            super.onTouchEvent(event)
        } else {
            animationHelper.onTouch(event)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        setEnabled(enabled, true)
    }

    internal fun onAnimate(dx: Float, dy: Float) {
        popDrawable.onAnimate(dx, dy)
    }

    internal fun animateShadow(dy: Float) {
        popDrawable.animateShadow(dy)
    }

    private fun setPadding() {
        if (!onLayoutCalled) {
            onLayoutCalled = true
            var bPadding = paddingBottom + depth.toInt()
            if (gravity == NeoPopGravity.ON_SPACE) {
                bPadding =
                    (paddingBottom + shadowHeight + depth).toInt()
            }
            val horizontalPadding =
                getMovePath(centerSurfaceRotation.toDouble(), height).toInt()
            updatePadding(
                left = paddingLeft + horizontalPadding,
                top = paddingTop,
                right = paddingRight + horizontalPadding,
                bottom = bPadding
            )
        }
    }

    private fun updateBackground() {
        background = popDrawable
    }

    private fun parseAttrsAndCreateStyle(
        attrs: AttributeSet?
    ) {
        withAttrs(attrs, R.styleable.TiltFrameLayout) {
            depth = getDimension(R.styleable.TiltFrameLayout_neopop_depth, 8.dp)
            centerSurfaceRotation = getFloat(
                R.styleable.TiltFrameLayout_neopop_center_surface_rotation,
                DEFAULT_CENTER_SURFACE_ROTATION
            )
            bottomSurfaceRotation = getFloat(
                R.styleable.TiltFrameLayout_neopop_bottom_surface_rotation,
                DEFAULT_BOTTOM_SURFACE_ROTATION
            )
            showShimmer = getBoolean(R.styleable.TiltFrameLayout_neopop_show_shimmer, false)
            animateOnTouch = getBoolean(
                R.styleable.TiltFrameLayout_neopop_animate_on_touch,
                TiltGeometry.ANIMATE_ON_TOUCH
            )
            centerSurfaceColor = getColor(
                R.styleable.TiltFrameLayout_neopop_center_surface_color,
                ContextCompat.getColor(context, R.color.white)
            )
            bottomSurfaceColor = getColor(
                R.styleable.TiltFrameLayout_neopop_bottom_surface_color,
                PopFrameLayoutStyle.getVerticalSurfaceColor(centerSurfaceColor)
            )
            gravity = NeoPopGravity.getValueFromInt(
                getInt(
                    R.styleable.TiltFrameLayout_neopop_gravity,
                    NeoPopGravity.ON_SPACE.value
                )
            )
            if (gravity == NeoPopGravity.ON_SPACE) {
                shadowTopMargin = getDimension(
                    R.styleable.TiltFrameLayout_neopop_shadow_top_margin,
                    0f
                )
                shadowHeight = getDimension(
                    R.styleable.TiltFrameLayout_neopop_shadow_height,
                    0f
                )
                shadowColor = getColor(
                    R.styleable.TiltFrameLayout_neopop_shadow_color,
                    TiltGeometry.DEFAULT_SHADOW_COLOR
                )
            }
            if (showShimmer) {
                shimmerStartDelay =
                    getInt(R.styleable.TiltFrameLayout_neopop_shimmer_start_delay, 0).toLong()
                isStrokeEnabled =
                    getBoolean(R.styleable.TiltFrameLayout_neopop_is_stroked_button, false)
                shimmerColor =
                    getColor(
                        R.styleable.TiltFrameLayout_neopop_shimmer_color,
                        TiltGeometry.SHIMMER_COLOR
                    )
                shimmerWidth = getDimension(
                    R.styleable.TiltFrameLayout_neopop_shimmer_width,
                    TiltGeometry.DEFAULT_SHIMMER_WIDTH
                )
                shimmerDuration = getInt(
                    R.styleable.TiltFrameLayout_neopop_shimmer_duration,
                    TiltGeometry.SHIMMER_ANIMATION_TIME
                )
                bottomShimmerColor = getColor(
                    R.styleable.TiltFrameLayout_neopop_bottom_shimmer_color,
                    Int.MIN_VALUE,
                )
                shimmerRepeatDelay =
                    getInt(
                        R.styleable.TiltFrameLayout_neopop_shimmer_repeat_delay,
                        0
                    ).toLong()
                if (bottomShimmerColor == Int.MIN_VALUE) {
                    bottomShimmerColor = PopFrameLayoutStyle.getBottomShimmer(shimmerColor)
                }
                if (isStrokeEnabled) {
                    strokeWidth = getDimension(
                        R.styleable.TiltFrameLayout_neopop_stroke_width,
                        0f
                    )
                    strokeColor =
                        getColor(
                            R.styleable.TiltFrameLayout_neopop_stroke_color,
                            TiltGeometry.STROKE_COLOR
                        )
                }
            }
        }

        tiltStyle = TiltFrameLayoutStyle(
            centerSurfaceColor = centerSurfaceColor,
            bottomSurfaceColor = bottomSurfaceColor,
            depth = depth,
            centerSurfaceRotation = centerSurfaceRotation,
            bottomSurfaceRotation = bottomSurfaceRotation,
            gravity = gravity,
            shimmerColor = shimmerColor,
            bottomShimmerColor = bottomShimmerColor,
            shimmerWidth = shimmerWidth,
            shimmerAnimationTime = shimmerDuration,
            hasShimmer = showShimmer,
            shadowHeight = shadowHeight,
            shimmerRepeatDelay = shimmerRepeatDelay,
            shimmerStartDelay = shimmerStartDelay,
            shadowColor = shadowColor,
            strokeWidth = strokeWidth,
            isStrokeEnabled = isStrokeEnabled,
            strokeColor = strokeColor
        )
    }

    private inline fun <T> onFieldChange(
        initialValue: T,
        crossinline onChange: TiltFrameLayoutStyle.(T) -> TiltFrameLayoutStyle
    ): ReadWriteProperty<Any?, T> {
        return object : ObservableProperty<T>(initialValue) {
            override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
                if (oldValue != newValue) {
                    tiltStyle = tiltStyle.onChange(newValue)
                }
            }
        }
    }
}
