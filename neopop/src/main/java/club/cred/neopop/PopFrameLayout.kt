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

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import club.cred.neopop.common.SHIMMER_ANIMATION_DURATION
import club.cred.neopop.common.dp
import club.cred.neopop.common.dynamicAttr
import club.cred.neopop.common.provideSafeHapticFeedback
import club.cred.neopop.common.withAttrs
import club.cred.neopop.popButton.Colors
import club.cred.neopop.popButton.NeoPopGeometry
import club.cred.neopop.popButton.NeoPopHelper
import club.cred.neopop.popButton.NeoPopHelper.calculateButtonStyles
import club.cred.neopop.popButton.PopDrawable
import club.cred.neopop.popButton.PopFrameLayoutStyle
import club.cred.neopop.popButton.SurfaceStrokeColorData
import club.cred.neopop.popButton.SurfaceStrokeColors
import com.dreamplug.neopop.R
import kotlin.math.roundToInt
import kotlin.properties.ReadWriteProperty

open class PopFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // /////////////////////////////////////////////////////////////////////////
    // Public Attrs
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Use [setCenterSurfaceColor] if all the surfaces are to be re-calculated
     */
    var centerSurfaceColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        setCenterSurfaceColor(it)
    }

    /**
     * height of top and bottom surface
     * width of left and right surface
     */
    var depth: Float = NeoPopGeometry.DEFAULT_DEPTH

    /**
     * Sets the colors for surfaces borders
     */
    var edgeColors: SurfaceStrokeColorData? by handleDynamicAttr(null) {
        popStyle = popStyle.copy(surfaceStrokeColors = it)
    }

    /**
     * sets the color for top surface
     */
    var topSurfaceColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle.copy(topSurfaceColor = it)
    }

    /**
     * sets the color for bottom surface
     */
    var bottomSurfaceColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle.copy(bottomSurfaceColor = it)
    }

    /**
     * sets the color for left surface
     */
    var leftSurfaceColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle.copy(leftSurfaceColor = it)
    }

    /**
     * sets the color for right surface
     */
    var rightSurfaceColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle.copy(rightSurfaceColor = it)
    }

    var surfaceColors: Colors by handleDynamicAttr(Colors()) {
        popStyle = popStyle.copy(
            topSurfaceColor = it.topColor ?: popStyle.topSurfaceColor,
            bottomSurfaceColor = it.bottomColor ?: popStyle.bottomSurfaceColor,
            leftSurfaceColor = it.leftColor ?: popStyle.leftSurfaceColor,
            rightSurfaceColor = it.rightColor ?: popStyle.rightSurfaceColor,
        )
    }

    /**
     * [parentViewColor] should be same as the background color of the view,
     * helps calculates surface colors for flat Buttons
     */
    var parentViewColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle
            .copy(parentViewColor = it)
            .calculateButtonStyles()
    }

    var grandParentViewColor: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle
            .copy(grandParentViewColor = it)
            .calculateButtonStyles()
    }

    /**
     * set this if there's a button on left, contains the target child id
     * this will recalculate the respective surface property
     */
    var buttonOnLeft: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle
            .copy(buttonOnLeft = it)
            .calculateButtonStyles()
    }

    /**
     * set this if there's a button on right, contains the target child id
     * this will recalculate the respective surface property
     */
    var buttonOnRight: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle
            .copy(buttonOnRight = it)
            .calculateButtonStyles()
    }

    /**
     * set this if there's a button on top, contains the target child id
     * this will recalculate the respective surface property
     */
    var buttonOnTop: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle
            .copy(buttonOnTop = it)
            .calculateButtonStyles()
    }

    /**
     * set this if there's a button on bottom, contains the target child id
     * this will recalculate the respective surface property
     */
    var buttonOnBottom: Int by handleDynamicAttr(Int.MIN_VALUE) {
        popStyle = popStyle
            .copy(buttonOnBottom = it)
            .calculateButtonStyles()
    }

    // ////////////////////////////////
    // Shimmer properties
    // ///////////////////////////////
    var shimmerColor: Int by handleDynamicAttr(Color.TRANSPARENT) {
        popStyle = popStyle.copy(shimmerColor = it)
    }
    var shimmerStartDelay: Long by handleDynamicAttr(0L) {
        popStyle = popStyle.copy(shimmerStartDelay = it)
    }
    var shimmerRepeatDelay: Long by handleDynamicAttr(0L) {
        popStyle = popStyle.copy(shimmerRepeatDelay = it)
    }

    /**
     * returns if the shimmer is animating or not
     */
    val isShimmerAnimationRunning: Boolean
        get() = popDrawable.isShimmerAnimating

    // /////////////////////////////////////////////////////////////////////////
    // Internal Variables
    // /////////////////////////////////////////////////////////////////////////

    private val popDrawable: PopDrawable = PopDrawable()
    private val buttonAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 50L
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                updateButtonState(animatedValue)
            }
        }
    }

    private var shouldPerformClick = false
    private var previousAnimationValue: Int = 0
    private var animateOnTouch: Boolean = false

    private var popStyle: PopFrameLayoutStyle = PopFrameLayoutStyle()
        set(value) {
            field = value
            disabledStyle = NeoPopHelper.getDisabledStateStyle(popStyle)
            popDrawable.popStyleData = if (this@PopFrameLayout.isEnabled) {
                value
            } else {
                disabledStyle
            }
        }
    private var disabledStyle: PopFrameLayoutStyle = NeoPopHelper.getDisabledStateStyle(popStyle)

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    init {
        parseAttrsAndCreateStyle(attrs)
        if (popStyle.hasShimmer) {
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
     * @param recalculateAllSurfaces - true, if all surface colors are to be recalculated
     * based on the card color. If custom surface colors are provided, then pass
     * [recalculateAllSurfaces] = false
     */
    fun setCenterSurfaceColor(cardColor: Int, recalculateAllSurfaces: Boolean = true) {
        popStyle =
            if (recalculateAllSurfaces) {
                popStyle.copy(centerSurfaceColor = cardColor).calculateButtonStyles()
            } else {
                popStyle.copy(centerSurfaceColor = cardColor)
            }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Internal and Overridden methods
    // /////////////////////////////////////////////////////////////////////////

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (popStyle.hasShimmer) {
            startShimmer()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopShimmer(stopImmediate = true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (!isEnabled || !isClickable) {
            super.onTouchEvent(event)
        } else {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    shouldPerformClick = true
                    if (animateOnTouch) {
                        buttonAnimator.start()
                    } else {
                        updateButtonState(1f)
                    }
                    this@PopFrameLayout.provideSafeHapticFeedback()
                    return true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_MOVE -> {
                    if (!shouldPerformClick) {
                        return true
                    }
                    if (event.x !in 0f..this@PopFrameLayout.width.toFloat() || event.y !in 0f..this@PopFrameLayout.height.toFloat()) {
                        shouldPerformClick = false
                        doUpAnimation()
                    } else if (event.action != MotionEvent.ACTION_MOVE) {
                        if (event.action == MotionEvent.ACTION_UP) {
                            this@PopFrameLayout.performClick()
                        }
                        doUpAnimation()
                    }
                    return true
                }
            }
            return false
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        popDrawable.popStyleData = if (enabled) popStyle else disabledStyle
    }

    private fun updateButtonState(animatedValue: Float) {
        val dx = animatedValue * depth
        val dy = animatedValue * depth
        popDrawable.onAnimate(dx, dy)
        popDrawable.onStrokeWidthAnimate(animatedValue)
        animatePadding(dx.toInt())
        this@PopFrameLayout.invalidate()
    }

    private fun doUpAnimation() {
        if (animateOnTouch) {
            buttonAnimator.reverse()
        } else {
            updateButtonState(0f)
        }
    }

    private fun animatePadding(animatedValue: Int) {
        val delta = animatedValue - previousAnimationValue
        this@PopFrameLayout.forEach {
            it.translationY += delta
            it.translationX += delta
        }
        previousAnimationValue = animatedValue
    }

    private fun parseAttrsAndCreateStyle(attrs: AttributeSet?) {
        this.withAttrs(attrs, R.styleable.PopFrameLayout) {
            centerSurfaceColor = getColor(
                R.styleable.PopFrameLayout_neopop_center_surface_color,
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            val strokeColor =
                getColor(R.styleable.PopFrameLayout_neopop_stroke_color, Int.MIN_VALUE)
            grandParentViewColor =
                getColor(
                    R.styleable.PopFrameLayout_neopop_grandparent_view_color,
                    Int.MIN_VALUE
                )
            parentViewColor =
                getColor(R.styleable.PopFrameLayout_neopop_parent_view_color, Int.MIN_VALUE)
            buttonOnTop =
                getResourceId(R.styleable.PopFrameLayout_neopop_button_on_top, Int.MIN_VALUE)
            buttonOnBottom =
                getResourceId(
                    R.styleable.PopFrameLayout_neopop_button_on_bottom,
                    Int.MIN_VALUE
                )
            buttonOnLeft =
                getResourceId(R.styleable.PopFrameLayout_neopop_button_on_left, Int.MIN_VALUE)
            buttonOnRight =
                getResourceId(
                    R.styleable.PopFrameLayout_neopop_button_on_right,
                    Int.MIN_VALUE
                )
            val disabledOpacity = getColor(
                R.styleable.PopFrameLayout_neopop_disabled_stroke_opacity,
                PopFrameLayoutStyle.disabledOpacity
            )
            val disabledCardColor = getColor(
                R.styleable.PopFrameLayout_neopop_disabled_card_color,
                PopFrameLayoutStyle.disabledCardColor
            )
            val disabledRightSurfaceColor = getColor(
                R.styleable.PopFrameLayout_neopop_disabled_right_surface_color,
                PopFrameLayoutStyle.disabledRightSurfaceColor
            )
            val disabledBottomSurfaceColor = getColor(
                R.styleable.PopFrameLayout_neopop_disabled_bottom_surface_color,
                PopFrameLayoutStyle.disabledBottomSurfaceColor
            )

            depth = getDimension(
                R.styleable.PopFrameLayout_neopop_depth,
                NeoPopGeometry.DEFAULT_DEPTH
            )
            val strokeWidth = getDimension(
                R.styleable.PopFrameLayout_neopop_stroke_width,
                NeoPopGeometry.DEFAULT_STROKE_WIDTH
            )
            val buttonPosition =
                getInt(R.styleable.PopFrameLayout_neopop_button_position, -1)
            val isStrokedButton =
                getBoolean(R.styleable.PopFrameLayout_neopop_is_stroked_button, false)
            shimmerRepeatDelay =
                getInt(R.styleable.PopFrameLayout_neopop_shimmer_repeat_delay, 0).toLong()
            shimmerStartDelay =
                getInt(R.styleable.PopFrameLayout_neopop_shimmer_start_delay, 0).toLong()

            val calculatedNeoPopStyle = NeoPopHelper.getCalculatedButtonStyles(
                mainCardColor = centerSurfaceColor,
                buttonPosition = buttonPosition,
                buttonOnRight = buttonOnRight,
                buttonOnLeft = buttonOnLeft,
                buttonOnTop = buttonOnTop,
                buttonOnBottom = buttonOnBottom,
                depth = depth,
                isStrokedButton = isStrokedButton,
                parentViewColor = parentViewColor,
                grandParentViewColor = grandParentViewColor,
            )

            bottomSurfaceColor = getColor(
                R.styleable.PopFrameLayout_neopop_bottom_surface_color,
                calculatedNeoPopStyle.bottomSurfaceColor
            )

            rightSurfaceColor =
                getColor(
                    R.styleable.PopFrameLayout_neopop_right_surface_color,
                    calculatedNeoPopStyle.rightSurfaceColor
                )

            topSurfaceColor = getColor(
                R.styleable.PopFrameLayout_neopop_top_surface_color,
                calculatedNeoPopStyle.topSurfaceColor
            )

            leftSurfaceColor =
                getColor(
                    R.styleable.PopFrameLayout_neopop_left_surface_color,
                    calculatedNeoPopStyle.leftSurfaceColor
                )
            animateOnTouch = getBoolean(
                R.styleable.PopFrameLayout_neopop_animate_on_touch,
                false
            )

            val isRightSurfaceVisible = getBoolean(
                R.styleable.PopFrameLayout_neopop_is_right_surface_visible,
                calculatedNeoPopStyle.isRightSurfaceVisible
            )
            val isTopSurfaceVisible = getBoolean(
                R.styleable.PopFrameLayout_neopop_is_top_surface_visible,
                calculatedNeoPopStyle.isTopSurfaceVisible
            )
            val isLeftSurfaceVisible = getBoolean(
                R.styleable.PopFrameLayout_neopop_is_left_surface_visible,
                calculatedNeoPopStyle.isLeftSurfaceVisible
            )
            val isBottomSurfaceVisible = getBoolean(
                R.styleable.PopFrameLayout_neopop_is_bottom_surface_visible,
                calculatedNeoPopStyle.isBottomSurfaceVisible
            )

            val showShimmer =
                getBoolean(R.styleable.PopFrameLayout_neopop_show_shimmer, false)
            shimmerColor =
                getColor(R.styleable.PopFrameLayout_neopop_shimmer_color, Color.TRANSPARENT)
            val shimmerWidth =
                getDimension(R.styleable.PopFrameLayout_neopop_shimmer_width, 0f.dp)
            val shimmerDuration = getInt(
                R.styleable.PopFrameLayout_neopop_shimmer_duration,
                SHIMMER_ANIMATION_DURATION.toInt()
            )
            val isBottomsheetSurface = getBoolean(
                R.styleable.PopFrameLayout_neopop_is_bottom_sheet_shadow,
                false
            )
            val drawFullHeight = getBoolean(
                R.styleable.PopFrameLayout_neopop_draw_full_height,
                false
            ) && buttonPosition == PopFrameLayoutStyle.center
            val drawFullWidth = getBoolean(
                R.styleable.PopFrameLayout_neopop_draw_full_width,
                false
            ) && buttonPosition == PopFrameLayoutStyle.center

            this@PopFrameLayout.background = popDrawable
            popStyle = popStyle.copy(
                centerSurfaceColor = centerSurfaceColor,
                topSurfaceColor = topSurfaceColor,
                bottomSurfaceColor = bottomSurfaceColor,
                rightSurfaceColor = rightSurfaceColor,
                leftSurfaceColor = leftSurfaceColor,
                isLeftSurfaceVisible = isLeftSurfaceVisible,
                isRightSurfaceVisible = isRightSurfaceVisible,
                isBottomSurfaceVisible = isBottomSurfaceVisible,
                isTopSurfaceVisible = isTopSurfaceVisible,
                buttonPosition = buttonPosition,
                depth = depth,
                isStrokedButton = isStrokedButton,
                buttonOnRight = buttonOnRight,
                buttonOnLeft = buttonOnLeft,
                buttonOnTop = buttonOnTop,
                buttonOnBottom = buttonOnBottom,
                shimmerWidth = shimmerWidth,
                shimmerColor = shimmerColor,
                hasShimmer = showShimmer,
                shimmerDuration = shimmerDuration.toLong(),
                strokeWidth = strokeWidth.roundToInt(),
                surfaceStrokeColors = if (strokeColor == Int.MIN_VALUE) {
                    null
                } else {
                    SurfaceStrokeColorData(
                        centerSurfaceStrokeColors = SurfaceStrokeColors(
                            strokeColor,
                            strokeColor,
                            strokeColor,
                            strokeColor
                        )
                    )
                },
                drawFullHeight = drawFullHeight,
                drawFullWidth = drawFullWidth,
                disabledOpacity = disabledOpacity,
                disabledCardColor = disabledCardColor,
                disabledBottomSurfaceColor = disabledBottomSurfaceColor,
                disabledRightSurfaceColor = disabledRightSurfaceColor,
                shimmerStartDelay = shimmerStartDelay,
                shimmerRepeatDelay = shimmerRepeatDelay,
                isBottomSheetSurface = isBottomsheetSurface
            )

            this@PopFrameLayout.forEach {
                it.translationY = (-depth.roundToInt()).toFloat()
                it.translationX = (-depth.roundToInt()).toFloat()
            }
        }
    }

    private inline fun <T> handleDynamicAttr(
        initialValue: T,
        crossinline onChange: (T) -> Unit = { }
    ): ReadWriteProperty<Any?, T> {
        return this@PopFrameLayout.dynamicAttr(initialValue) {
            onChange(it)
        }
    }
}
