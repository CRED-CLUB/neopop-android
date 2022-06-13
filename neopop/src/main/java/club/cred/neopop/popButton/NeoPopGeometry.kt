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

import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.util.Size
import club.cred.neopop.common.Quadrilateral
import club.cred.neopop.common.Stroke
import club.cred.neopop.common.dp
import kotlin.math.roundToInt
import kotlin.math.tan

class NeoPopGeometry(
    val size: Size,
    val totalDepth: Float,
    val clipRight: Boolean,
    val clipBottom: Boolean,
    val viewBoundRect: Rect,
    val isBottomSheet: Boolean = false,
    val shimmerWidth: Float = 0f.dp,
    val drawFullWidth: Boolean = true,
    val drawFullHeight: Boolean = true
) {
    private val topPlane = Quadrilateral(
        leftTop = PointF(0f, 0f),
        rightTop = PointF(getRectWidth(), 0f),
        rightBottom = PointF(
            getRectWidth(),
            getRectHeight()
        ),
        leftBottom = PointF(0f, getRectHeight())
    )

    private fun getRectWidth(): Float {
        return if (drawFullWidth) size.width.toFloat() else size.width - totalDepth
    }

    private fun getRectHeight(): Float {
        return if (drawFullHeight) size.height.toFloat() else size.height - totalDepth
    }

    private val bottomPlane = topPlane.translate(totalDepth, totalDepth)

    val mainSurface: Quadrilateral
        get() =
            when {
                !isBottomSheet -> topPlane
                else -> Quadrilateral(
                    leftTop = PointF(0f, totalDepth),
                    rightTop = PointF(size.width.toFloat(), totalDepth),
                    rightBottom = PointF(size.width.toFloat(), size.height.toFloat()),
                    leftBottom = PointF(0f, size.height.toFloat())
                )
            }

    val startShadow: Quadrilateral by lazy {
        Quadrilateral(
            leftTop = topPlane.leftBottom,
            rightTop = topPlane.leftTop,
            rightBottom = bottomPlane.leftTop,
            leftBottom = bottomPlane.leftBottom
        )
    }

    val topShadow: Quadrilateral by lazy {
        if (!isBottomSheet) {
            Quadrilateral(
                leftTop = topPlane.leftTop,
                rightTop = topPlane.rightTop,
                rightBottom = bottomPlane.rightTop,
                leftBottom = bottomPlane.leftTop
            )
        } else {
            val angle = 45.toDouble()
            val theta =
                tan(Math.toRadians(angle))
            val movePath = (totalDepth * theta).roundToInt()

            val trapezium = Quadrilateral(
                leftTop = PointF(movePath.toFloat(), 0f),
                rightTop = PointF(size.width.minus(movePath).toFloat(), 0f),
                rightBottom = PointF(size.width.toFloat(), totalDepth),
                leftBottom = PointF(0f, totalDepth)
            )
            trapezium
        }
    }
    private val distanceBwShimmerRect by lazy {
        shimmerWidth * 0.25f
    }

    val shimmerMovePathAdjustment by lazy {
        shimmerWidth.let {
            val angle = SHIMMER_ANGLE
            val theta =
                tan(Math.toRadians(angle))
            val movePath = (it * theta).roundToInt()
            it + BIG_SHIMMER_RECT * it + THIN_SHIMMER_RECT * it + distanceBwShimmerRect + movePath
        }
    }

    val shimmerPath: Quadrilateral by lazy {
        val angle = SHIMMER_ANGLE
        val theta =
            tan(Math.toRadians(angle))
        val movePath = (shimmerWidth * theta).roundToInt()
        val partShimmerWidth = shimmerWidth * BIG_SHIMMER_RECT

        Quadrilateral(
            leftTop = PointF(topPlane.leftTop.x.minus(partShimmerWidth), topPlane.leftTop.y),
            rightTop = PointF(topPlane.leftTop.x, topPlane.leftTop.y),
            rightBottom = PointF(topPlane.leftBottom.x.minus(movePath), topPlane.leftBottom.y),
            leftBottom = PointF(
                topPlane.leftBottom.x.minus(partShimmerWidth + movePath),
                topPlane.leftBottom.y
            ),
        )
    }
    val thinShimmer: Quadrilateral by lazy {
        val partShimmerWidth = shimmerWidth * THIN_SHIMMER_RECT
        Quadrilateral(
            leftTop = PointF(
                shimmerPath.leftTop.x.minus(partShimmerWidth).minus(distanceBwShimmerRect),
                shimmerPath.leftTop.y
            ),
            rightTop = PointF(
                shimmerPath.leftTop.x.minus(distanceBwShimmerRect),
                shimmerPath.leftTop.y
            ),
            rightBottom = PointF(
                shimmerPath.leftBottom.x.minus(distanceBwShimmerRect),
                topPlane.leftBottom.y
            ),
            leftBottom = PointF(
                shimmerPath.leftBottom.x.minus(partShimmerWidth).minus(distanceBwShimmerRect),
                topPlane.leftBottom.y
            ),
        )
    }

    val bottomShadow = Quadrilateral(
        leftTop = topPlane.leftBottom,
        rightTop = topPlane.rightBottom,
        rightBottom = bottomPlane.rightBottom,
        leftBottom = bottomPlane.leftBottom
    )

    val endShadow = Quadrilateral(
        leftTop = topPlane.rightTop,
        rightTop = bottomPlane.rightTop,
        rightBottom = bottomPlane.rightBottom,
        leftBottom = topPlane.rightBottom
    )

    val stroke1 by lazy {
        Stroke(
            PointF(topPlane.leftBottom.x, topPlane.leftBottom.y.minus(DEFAULT_STROKE_WIDTH / 2)),
            PointF(topPlane.rightBottom.x, topPlane.rightBottom.y.minus(DEFAULT_STROKE_WIDTH / 2))
        )
    }
    val stroke2 by lazy {
        Stroke(
            PointF(topPlane.rightBottom.x.minus(DEFAULT_STROKE_WIDTH / 2), topPlane.rightBottom.y),
            PointF(topPlane.rightTop.x.minus(DEFAULT_STROKE_WIDTH / 2), topPlane.rightTop.y)
        )
    }

    val stroke3 by lazy {
        Stroke(
            PointF(topPlane.rightTop.x, topPlane.rightTop.y.plus(DEFAULT_STROKE_WIDTH / 2)),
            PointF(topPlane.leftTop.x, topPlane.leftTop.y.plus(DEFAULT_STROKE_WIDTH / 2)),
        )
    }

    val stroke4 by lazy { Stroke(PointF(0f, 0f), PointF(0f, 0f)) }

    val stroke5 by lazy {
        Stroke(
            PointF(endShadow.rightTop.x.minus(DEFAULT_STROKE_WIDTH / 2), endShadow.rightTop.y),
            PointF(endShadow.rightBottom.x.minus(DEFAULT_STROKE_WIDTH / 2), endShadow.rightBottom.y)
        )
    }

    val stroke6 by lazy {
        Stroke(
            PointF(
                bottomShadow.rightBottom.x,
                bottomShadow.rightBottom.y.minus(DEFAULT_STROKE_WIDTH / 2)
            ),
            PointF(
                bottomShadow.leftBottom.x,
                bottomShadow.leftBottom.y.minus(DEFAULT_STROKE_WIDTH / 2)
            )
        )
    }

    val stroke7 by lazy {
        Stroke(
            PointF(0f, 0f),
            PointF(0f, 0f)
        )
    }

    val stroke8 by lazy {
        Stroke(
            PointF(topPlane.leftBottom.x.plus(DEFAULT_STROKE_WIDTH / 2), topPlane.leftBottom.y),
            PointF(topPlane.leftTop.x.plus(DEFAULT_STROKE_WIDTH / 2), topPlane.leftTop.y)
        )
    }

    var clipPath: Path? = null

    init {
        if (clipRight || clipBottom) {
            clipPath = Path().apply {
                moveTo(0f, 0f)
                if (clipRight) {
                    lineTo(topPlane.rightTop.x, topPlane.rightTop.y)
                    lineTo(topPlane.rightBottom.x, topPlane.rightBottom.y)
                    if (clipBottom) {
                        lineTo(topPlane.leftBottom.x, topPlane.leftBottom.y)
                    } else {
                        lineTo(bottomPlane.rightBottom.x, bottomPlane.rightBottom.y)
                        lineTo(0f, size.height.toFloat())
                    }
                } else {
                    lineTo(size.width.toFloat(), 0f)
                    lineTo(size.width.toFloat(), size.height.toFloat())
                    if (clipBottom) {
                        lineTo(topPlane.rightBottom.x, topPlane.rightBottom.y)
                        lineTo(topPlane.leftBottom.x, topPlane.leftBottom.y)
                    } else {
                        lineTo(0f, size.height.toFloat())
                    }
                }
                close()
            }
        }
    }

    companion object {
        val DEFAULT_SHADOW_WIDTH: Float = 3f.dp
        val DEFAULT_STROKE_WIDTH: Float = 1f.dp
        const val BIG_SHIMMER_RECT = 0.9f
        const val THIN_SHIMMER_RECT = 0.3f
        const val SHIMMER_ANGLE = 45.toDouble()
    }
}
