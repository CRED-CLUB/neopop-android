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

package club.cred.neopop.popButton

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.Size
import androidx.core.graphics.withClip
import club.cred.neopop.common.NeoButtonDrawableInteractor
import club.cred.neopop.common.drawStroke
import club.cred.neopop.common.getFillPaint
import club.cred.neopop.common.translateWith
import club.cred.neopop.popButton.NeoPopGeometry.Companion.DEFAULT_STROKE_WIDTH
import club.cred.neopop.common.ShimmerAnimationHelper
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal class PopDrawable(
    data: PopFrameLayoutStyle = PopFrameLayoutStyle()
) :
    Drawable(),
    NeoButtonDrawableInteractor {

    var popStyleData: PopFrameLayoutStyle = data
        set(value) {
            field = value
            topShadowPaint.color = popStyleData.topSurfaceColor
            leftShadowPaint.color = popStyleData.leftSurfaceColor
            bottomShadowPaint.color = popStyleData.bottomSurfaceColor
            rightShadowPaint.color = popStyleData.rightSurfaceColor
            centerCardPaint.color = popStyleData.centerSurfaceColor
            shimmerColor.color = popStyleData.shimmerColor
            shimmerAnimationHelper.repeatDelay = popStyleData.shimmerRepeatDelay
            shimmerAnimationHelper.startDelay = popStyleData.shimmerStartDelay
            invalidateSelf()
        }

    internal val isShimmerAnimating: Boolean
        get() = shimmerAnimationHelper.isShimmerAnimating

    private val topShadowPaint = getFillPaint(popStyleData.topSurfaceColor)
    private val leftShadowPaint = getFillPaint(popStyleData.leftSurfaceColor)
    private val bottomShadowPaint = getFillPaint(popStyleData.bottomSurfaceColor)
    private val rightShadowPaint = getFillPaint(popStyleData.rightSurfaceColor)
    private val centerCardPaint = getFillPaint(popStyleData.centerSurfaceColor)
    private val shimmerColor =
        getFillPaint(popStyleData.shimmerColor).apply {
            isAntiAlias = true
        }

    private fun getStrokePaint(strokeColor: Int): Paint = Paint().apply {
        color = strokeColor
        strokeWidth = DEFAULT_STROKE_WIDTH
    }

    private lateinit var neoPopGeometry: NeoPopGeometry
    private val shimmerAnimationHelper by lazy {
        ShimmerAnimationHelper(
            popStyleData.shimmerDuration,
            this,
        ).apply {
            repeatDelay = popStyleData.shimmerRepeatDelay
            startDelay = popStyleData.shimmerStartDelay
        }
    }
    private var isDrawableEnabled = false
        set(value) {
            if (field != value) {
                field = value
                shimmerAnimationHelper.isDrawableEnabled = value
            }
        }

    private var dx = 0f
    private var dy = 0f
    private var pressFraction = 0f
    private var shimmerDx = 0f
    private var showShimmer = false
    override fun onAnimate(dx: Float, dy: Float) {
        this.dx = dx
        this.dy = dy
    }

    override fun onStrokeWidthAnimate(strokeWidthValue: Float) {
        this.pressFraction = strokeWidthValue
    }

    override fun onShimmerAnimation(animatedValue: Float, animatedPercentage: Float) {
        shimmerDx = animatedValue
        invalidateSelf()
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        if (bounds == null) return
        neoPopGeometry = NeoPopGeometry(
            size = Size(bounds.width(), bounds.height()),
            totalDepth = popStyleData.shadowWidth.roundToInt().toFloat(),
            clipRight = !popStyleData.isRightSurfaceVisible,
            clipBottom = !popStyleData.isBottomSurfaceVisible,
            viewBoundRect = bounds,
            isBottomSheet = popStyleData.isBottomSheetShadow,
            shimmerWidth = popStyleData.shimmerWidth,
            drawFullWidth = popStyleData.drawFullWidth,
            drawFullHeight = popStyleData.drawFullHeight
        )
        if (popStyleData.hasShimmer) {
            shimmerAnimationHelper.width =
                (bounds.width() + neoPopGeometry.shimmerMovePathAdjustment.roundToInt())
        }
    }

    override fun draw(canvas: Canvas) {
        if (!::neoPopGeometry.isInitialized) {
            return
        }
        canvas.clipPop {
            if (popStyleData.isBottomSheetShadow || dx > 0 || dy > 0) {
                if (popStyleData.isLeftSurfaceVisible) {
                    drawPath(neoPopGeometry.startShadow.fullOutline, leftShadowPaint)
                }
                if (popStyleData.isTopSurfaceVisible) {
                    drawPath(neoPopGeometry.topShadow.fullOutline, topShadowPaint)
                }
            }
            translateWith(dx, dy) {
                if (popStyleData.isRightSurfaceVisible) {
                    drawPath(neoPopGeometry.endShadow.fullOutline, rightShadowPaint)
                }
                if (popStyleData.isBottomSurfaceVisible && !popStyleData.isBottomSheetShadow) {
                    drawPath(
                        neoPopGeometry.bottomShadow.fullOutline,
                        bottomShadowPaint
                    )
                }
                drawPath(neoPopGeometry.mainSurface.fullOutline, centerCardPaint)

                if (popStyleData.hasShimmer && showShimmer) {
                    withClip(neoPopGeometry.mainSurface.fullOutline) {
                        drawShimmer(this)
                    }
                }

                drawEdges(this)
            }
            if (popStyleData.isStrokedButton) {
                drawConstantEdges(this)
            }
        }
    }

    private fun drawConstantEdges(canvas: Canvas) {
        popStyleData.surfaceStrokeColors?.rightSurfaceStrokeColors?.rightColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke5, getStrokePaint(it))
        }

        popStyleData.surfaceStrokeColors?.bottomSurfaceStrokeColors?.bottomColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke6, getStrokePaint(it))
        }
    }

    private fun drawShimmer(canvas: Canvas) {
        canvas.translateWith(shimmerDx, 0f) {
            canvas.drawPath(neoPopGeometry.shimmerPath.fullOutline, shimmerColor)
            canvas.drawPath(neoPopGeometry.thinShimmer.fullOutline, shimmerColor)
        }
    }

    fun startShimmer() {
        if (popStyleData.hasShimmer) {
            showShimmer = true
            shimmerAnimationHelper.startShimmer()
        }
    }

    fun stopShimmer(stopImmediate: Boolean = false, onEnd: (() -> Unit)? = null) {
        if (popStyleData.hasShimmer) {
            shimmerAnimationHelper.stopShimmer(stopImmediate) {
                showShimmer = false
                onEnd?.invoke()
            }
        }
    }

    private fun drawEdges(canvas: Canvas) {
        val slantedLineStrokeWidth = (DEFAULT_STROKE_WIDTH * (1 - pressFraction)) +
            (pressFraction * DEFAULT_STROKE_WIDTH / SQRT_2)
        val slantedLineStrokeWidthSqrt = slantedLineStrokeWidth / (SQRT_2 * 2)

        popStyleData.surfaceStrokeColors?.centerSurfaceStrokeColors?.bottomColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke1, getStrokePaint(it))
        }

        popStyleData.surfaceStrokeColors?.centerSurfaceStrokeColors?.rightColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke2, getStrokePaint(it))
        }

        popStyleData.surfaceStrokeColors?.centerSurfaceStrokeColors?.topColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke3, getStrokePaint(it))
        }

        popStyleData.surfaceStrokeColors?.rightSurfaceStrokeColors?.topColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawLine(
                    neoPopGeometry.endShadow.leftTop.x - slantedLineStrokeWidthSqrt,
                    neoPopGeometry.endShadow.leftTop.y + slantedLineStrokeWidthSqrt,
                    neoPopGeometry.endShadow.rightTop.x - slantedLineStrokeWidthSqrt,
                    neoPopGeometry.endShadow.rightTop.y + slantedLineStrokeWidthSqrt,
                    Paint().apply {
                        color = it
                        strokeWidth = slantedLineStrokeWidth
                    }
                )
        }

        popStyleData.surfaceStrokeColors?.rightSurfaceStrokeColors?.rightColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke5, getStrokePaint(it))
        }

        popStyleData.surfaceStrokeColors?.bottomSurfaceStrokeColors?.bottomColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke6, getStrokePaint(it))
        }

        popStyleData.surfaceStrokeColors?.bottomSurfaceStrokeColors?.leftColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawLine(
                    neoPopGeometry.bottomShadow.leftBottom.x + slantedLineStrokeWidthSqrt,
                    neoPopGeometry.bottomShadow.leftBottom.y - slantedLineStrokeWidthSqrt,
                    neoPopGeometry.bottomShadow.leftTop.x + slantedLineStrokeWidthSqrt,
                    neoPopGeometry.bottomShadow.leftTop.y - slantedLineStrokeWidthSqrt,
                    Paint().apply {
                        color = it
                        strokeWidth = slantedLineStrokeWidth
                    }
                )
        }

        popStyleData.surfaceStrokeColors?.centerSurfaceStrokeColors?.leftColor?.let {
            if (it != Int.MIN_VALUE)
                canvas.drawStroke(neoPopGeometry.stroke8, getStrokePaint(it))
        }
    }

    override fun onStateChange(state: IntArray?): Boolean {
        isDrawableEnabled = !(
            state?.contains(-android.R.attr.state_enabled) == true ||
                state?.contains(android.R.attr.state_enabled) == false
            )
        return super.onStateChange(state)
    }

    override fun isStateful(): Boolean {
        return true
    }

    private fun Canvas.clipPop(block: Canvas.() -> Unit) {
        if (neoPopGeometry.clipPath != null) {
            translateWith(
                neoPopGeometry.viewBoundRect.left.toFloat(),
                neoPopGeometry.viewBoundRect.top.toFloat()
            ) {
                clipPath(neoPopGeometry.clipPath!!)
                block()
            }
        } else {
            clipRect(RectF(neoPopGeometry.viewBoundRect))
            translateWith(
                neoPopGeometry.viewBoundRect.left.toFloat(),
                neoPopGeometry.viewBoundRect.top.toFloat()
            ) {
                block()
            }
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    companion object {
        val SQRT_2 = sqrt(2f)
    }
}
