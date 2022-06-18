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

package club.cred.neopop.tiltButton

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Size
import club.cred.neopop.common.NeoButtonDrawableInteractor
import club.cred.neopop.common.ShimmerAnimationHelper
import club.cred.neopop.common.drawStroke
import club.cred.neopop.common.translateWith

internal class TiltDrawable(
    data: TiltFrameLayoutStyle = TiltFrameLayoutStyle()
) :
    Drawable(),
    NeoButtonDrawableInteractor {

    var tiltStyle: TiltFrameLayoutStyle = data
        set(value) {
            field = value
            bottomSurfacePaint.color = tiltStyle.bottomSurfaceColor
            centerSurfacePaint.color = tiltStyle.centerSurfaceColor
            shadowPaint.color = tiltStyle.shadowColor
            shimmerPaint.color = tiltStyle.shimmerColor
            bottomShimmerPaint.color = tiltStyle.bottomShimmerColor
            strokePaint = createStrokePaint(tiltStyle.strokeColor)
            shimmerAnimationHelper.repeatDelay = tiltStyle.shimmerRepeatDelay
            shimmerAnimationHelper.startDelay = tiltStyle.shimmerStartDelay
            invalidateSelf()
        }

    internal val isShimmerAnimating: Boolean
        get() = shimmerAnimationHelper.isShimmerAnimating

    private var dx = 0f
    private var dy = 0f
    private var shadowY = 0f
    private var shimmerAnimatedValue = 0f
    private var bottomSurfacePaint: Paint = getFillPaint(tiltStyle.bottomSurfaceColor)
    private var centerSurfacePaint: Paint = getFillPaint(tiltStyle.centerSurfaceColor)
    private var shadowPaint: Paint = getFillPaint(Color.BLACK)
    private var shimmerPaint: Paint = getFillPaint(tiltStyle.shimmerColor)
    private var bottomShimmerPaint: Paint = getFillPaint(tiltStyle.bottomShimmerColor)
    private var strokePaint: Paint = createStrokePaint(tiltStyle.strokeColor)

    private lateinit var neoPopGeometry: TiltGeometry

    private val shimmerAnimationHelper: ShimmerAnimationHelper by lazy {
        ShimmerAnimationHelper(tiltStyle.shimmerAnimationTime.toLong(), this)
            .apply {
                repeatDelay = tiltStyle.shimmerRepeatDelay
                startDelay = tiltStyle.shimmerStartDelay
            }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        if (bounds == null) return
        neoPopGeometry = TiltGeometry(
            size = Size(bounds.width(), bounds.height()),
            data = tiltStyle,
        )
        shimmerAnimationHelper.width = bounds.width() + tiltStyle.shimmerWidth.toInt()
    }

    override fun draw(canvas: Canvas) {
        if (!::neoPopGeometry.isInitialized) return
        canvas.drawFloatingShadow()
        canvas.drawBottomShadow()
        canvas.drawCenterCard()
        canvas.drawShimmer()
        canvas.drawStroke()
    }

    private fun getFillPaint(paintColor: Int): Paint =
        Paint().apply {
            style = Paint.Style.FILL
            this.color = paintColor
            isAntiAlias = true
            isDither = true
        }

    private fun createStrokePaint(strokeColor: Int): Paint = Paint().apply {
        color = strokeColor
        style = Paint.Style.STROKE
        strokeWidth = tiltStyle.strokeWidth
    }

    private fun Canvas.drawBottomShadow() {
        translateWith(dx, dy) {
            this.drawPath(neoPopGeometry.bottomShadowPlane.fullOutline, bottomSurfacePaint)
        }
    }

    private fun Canvas.drawCenterCard() {
        translateWith(dx, dy) {
            this.drawPath(neoPopGeometry.mainPlane.fullOutline, centerSurfacePaint)
        }
    }

    private fun Canvas.drawStroke() {
        if (tiltStyle.isStrokeEnabled) {
            translateWith(dx, dy) {
                drawStroke(neoPopGeometry.mainPlaneStroke.fullOutline, strokePaint)
                drawStroke(neoPopGeometry.bottomPlaneStroke.fullOutline, strokePaint)
            }
        }
    }

    private fun Canvas.drawFloatingShadow() {
        if (tiltStyle.gravity == NeoPopGravity.ON_SPACE) {
            translateWith(dx, -shadowY) {
                this.drawPath(neoPopGeometry.blackShadowPlane.fullOutline, shadowPaint)
            }
        }
    }

    private fun Canvas.drawShimmer() {
        if (tiltStyle.hasShimmer && isShimmerAnimating) {
            clipPop {
                translateWith(shimmerAnimatedValue, dy) {
                    this.drawPath(neoPopGeometry.bigShimmerTopPlane.fullOutline, shimmerPaint)
                }
                translateWith(shimmerAnimatedValue, dy) {
                    this.drawPath(
                        neoPopGeometry.bigShimmerBottomPlane.fullOutline,
                        bottomShimmerPaint
                    )
                }
                translateWith(shimmerAnimatedValue, dy) {
                    this.drawPath(neoPopGeometry.smallShimmerTopPlane.fullOutline, shimmerPaint)
                }
                translateWith(shimmerAnimatedValue, dy) {
                    this.drawPath(
                        neoPopGeometry.smallShimmerBottomPlane.fullOutline,
                        bottomShimmerPaint
                    )
                }
            }
        }
    }

    private inline fun Canvas.clipPop(block: () -> Unit) {
        if (tiltStyle.hasShimmer && isShimmerAnimating) {
            translateWith(dx, dy) {
                clipPath(neoPopGeometry.clipPath.fullOutline)
            }
        }
        block()
    }

    fun startShimmer() {
        if (tiltStyle.hasShimmer) {
            shimmerAnimationHelper.startShimmer()
        }
    }

    fun stopShimmer(stopImmediate: Boolean = false, onEnd: (() -> Unit)? = null) {
        if (tiltStyle.hasShimmer) {
            shimmerAnimationHelper.stopShimmer(stopImmediate) {
                onEnd?.invoke()
                invalidateSelf()
            }
        }
    }

    fun animateShadow(dy: Float) {
        this.shadowY = dy
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun onStrokeWidthAnimate(strokeWidthValue: Float) {}

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun onStateChange(state: IntArray?): Boolean {
        val isDrawableEnabled = !(
            state?.contains(-android.R.attr.state_enabled) == true ||
                state?.contains(android.R.attr.state_enabled) == false
            )
        val isUpdated = shimmerAnimationHelper.isDrawableEnabled != isDrawableEnabled
        shimmerAnimationHelper.isDrawableEnabled = isDrawableEnabled
        return isUpdated
    }

    override fun isStateful(): Boolean {
        return true
    }

    override fun onAnimate(dx: Float, dy: Float) {
        this.dx = dx
        this.dy = dy
        invalidateSelf()
    }

    override fun onShimmerAnimation(animatedValue: Float, animatedPercentage: Float) {
        shimmerAnimatedValue = animatedValue
        neoPopGeometry.updateShimmerPlanes(animatedPercentage)
        invalidateSelf()
    }
}
