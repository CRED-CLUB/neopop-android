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

import android.graphics.Color
import android.graphics.PointF
import android.util.Size
import club.cred.neopop.common.OpenPolygon
import club.cred.neopop.common.Polygon
import club.cred.neopop.common.Quadrilateral
import club.cred.neopop.common.dp
import club.cred.neopop.common.getMovePath
import kotlin.math.roundToInt

internal class TiltGeometry(
    private val size: Size,
    data: TiltFrameLayoutStyle
) {

    private val strokeWidth = data.strokeWidth
    private val totalDepth = data.depth.roundToInt().toFloat()
    private val gravity = data.gravity
    private val shimmerWidth = data.shimmerWidth
    private val shadowHeight: Float = data.shadowHeight
    private val centerSurfaceMovePath =
        getMovePath(data.centerSurfaceRotation.toDouble(), size.height)
    private val bottomSurfaceMovePath =
        getMovePath(data.bottomSurfaceRotation.toDouble(), totalDepth.toInt())
    private val bottomSpacing =
        if (gravity == NeoPopGravity.ON_GROUND) {
            totalDepth
        } else {
            totalDepth + shadowHeight
        }

    val mainPlane = Quadrilateral(
        leftTop = PointF(centerSurfaceMovePath, 0f),
        rightTop = PointF(size.width - centerSurfaceMovePath, 0f),
        rightBottom = PointF(size.width.toFloat(), size.height - bottomSpacing),
        leftBottom = PointF(0f, size.height - bottomSpacing)
    )

    val bottomSurfacePlane = Quadrilateral(
        leftTop = mainPlane.leftBottom,
        rightTop = mainPlane.rightBottom,
        rightBottom = PointF(
            size.width.toFloat() - bottomSurfaceMovePath,
            mainPlane.rightBottom.y + totalDepth
        ),
        leftBottom = PointF(bottomSurfaceMovePath, mainPlane.leftBottom.y + totalDepth)
    )

    val mainPlaneStroke = Polygon(
        listOf(
            PointF(
                mainPlane.leftTop.x,
                mainPlane.leftTop.y + strokeWidth / 2
            ),
            PointF(
                mainPlane.leftBottom.x + strokeWidth / 2,
                mainPlane.leftBottom.y - strokeWidth / 2
            ),
            PointF(
                mainPlane.rightBottom.x - strokeWidth / 2,
                mainPlane.leftBottom.y - strokeWidth / 2
            ),
            PointF(mainPlane.rightTop.x, mainPlane.rightTop.y + strokeWidth / 2),
        )
    )

    val bottomPlaneStroke = OpenPolygon(
        listOf(
            PointF(
                bottomSurfacePlane.leftTop.x + strokeWidth / 2,
                bottomSurfacePlane.leftTop.y
            ),
            PointF(
                bottomSurfacePlane.leftBottom.x + strokeWidth / 2,
                bottomSurfacePlane.leftBottom.y - strokeWidth / 2
            ),
            PointF(
                bottomSurfacePlane.rightBottom.x - strokeWidth / 2,
                bottomSurfacePlane.rightBottom.y - strokeWidth / 2
            ),
            PointF(bottomSurfacePlane.rightTop.x - strokeWidth / 2, bottomSurfacePlane.rightTop.y),
        )
    )

    val bigShimmerTopPlane by lazy {
        Quadrilateral(
            rightTop = mainPlane.leftTop,
            leftTop = PointF(
                mainPlane.leftTop.x - (shimmerWidth * BIG_SHIMMER_WIDTh_PERCENTAGE).toInt(),
                mainPlane.rightTop.y
            ),
            rightBottom = mainPlane.leftBottom,
            leftBottom = PointF(
                mainPlane.leftBottom.x - (shimmerWidth * BIG_SHIMMER_WIDTh_PERCENTAGE).toInt(),
                mainPlane.rightBottom.y
            )
        )
    }
    val bigShimmerBottomPlane by lazy {
        Quadrilateral(
            leftTop = bigShimmerTopPlane.leftBottom,
            rightTop = bigShimmerTopPlane.rightBottom,
            rightBottom = bottomSurfacePlane.leftBottom,
            leftBottom = PointF(
                bottomSurfacePlane.leftBottom.x - (shimmerWidth * BIG_SHIMMER_WIDTh_PERCENTAGE).toInt(),
                bottomSurfacePlane.leftBottom.y
            )
        )
    }

    val smallShimmerTopPlane by lazy {
        Quadrilateral(
            rightTop = PointF(
                bigShimmerTopPlane.leftTop.x - (shimmerWidth * SHIMMER_SPACING_PERCENTAGE).toInt(),
                bigShimmerTopPlane.leftTop.y
            ),
            leftTop = PointF(
                bigShimmerTopPlane.leftTop.x - (shimmerWidth * SMALL_SHIMMER_WIDTH_PERCENTAGE).toInt() - (shimmerWidth * SHIMMER_SPACING_PERCENTAGE).toInt(),
                bigShimmerTopPlane.rightTop.y
            ),
            rightBottom = PointF(
                bigShimmerTopPlane.leftBottom.x - (shimmerWidth * SHIMMER_SPACING_PERCENTAGE).toInt(),
                bigShimmerTopPlane.leftBottom.y
            ),
            leftBottom = PointF(
                bigShimmerTopPlane.leftBottom.x - (shimmerWidth * SMALL_SHIMMER_WIDTH_PERCENTAGE).toInt() - (shimmerWidth * SHIMMER_SPACING_PERCENTAGE).toInt(),
                bigShimmerTopPlane.rightBottom.y
            )
        )
    }
    val smallShimmerBottomPlane by lazy {
        Quadrilateral(
            leftTop = smallShimmerTopPlane.leftBottom,
            rightTop = smallShimmerTopPlane.rightBottom,
            rightBottom = PointF(
                bigShimmerBottomPlane.leftBottom.x - (shimmerWidth * SHIMMER_SPACING_PERCENTAGE).toInt(),
                bigShimmerBottomPlane.leftBottom.y
            ),
            leftBottom = PointF(
                bigShimmerBottomPlane.leftBottom.x -
                        (shimmerWidth * SMALL_SHIMMER_WIDTH_PERCENTAGE).toInt() -
                        (shimmerWidth * SHIMMER_SPACING_PERCENTAGE).toInt(),
                bigShimmerBottomPlane.leftBottom.y
            )
        )
    }

    val shadowPlane by lazy {
        Quadrilateral(
            leftTop = PointF(
                bottomSurfacePlane.leftBottom.x + centerSurfaceMovePath,
                shadowHeight + totalDepth
            ),
            rightTop = PointF(
                bottomSurfacePlane.rightBottom.x - centerSurfaceMovePath,
                shadowHeight + totalDepth
            ),
            rightBottom = PointF(bottomSurfacePlane.rightBottom.x, size.height.toFloat()),
            leftBottom = PointF(bottomSurfacePlane.leftBottom.x, size.height.toFloat())
        )
    }

    val clipPath by lazy {
        Polygon(
            arrayListOf(
                mainPlane.leftTop,
                mainPlane.rightTop,
                mainPlane.rightBottom,
                bottomSurfacePlane.rightBottom,
                bottomSurfacePlane.leftBottom,
                mainPlane.leftBottom
            )
        )
    }

    fun updateShimmerPlanes(percentage: Float = -1f) {
        val shimmerAnimationPercentage = percentage * 2
        bigShimmerTopPlane.resetPathWithNewPoints(
            listOf(
                PointF(
                    bigShimmerTopPlane.leftTop.x - (centerSurfaceMovePath * shimmerAnimationPercentage),
                    bigShimmerTopPlane.leftTop.y
                ),
                PointF(
                    bigShimmerTopPlane.rightTop.x - (centerSurfaceMovePath * shimmerAnimationPercentage),
                    bigShimmerTopPlane.rightTop.y
                ),
                bigShimmerTopPlane.rightBottom,
                bigShimmerTopPlane.leftBottom,
            )
        )
        bigShimmerBottomPlane.resetPathWithNewPoints(
            listOf(
                bigShimmerBottomPlane.leftTop,
                bigShimmerBottomPlane.rightTop,
                PointF(
                    bigShimmerBottomPlane.rightBottom.x - (bottomSurfaceMovePath * shimmerAnimationPercentage),
                    bigShimmerBottomPlane.rightBottom.y
                ),
                PointF(
                    bigShimmerBottomPlane.leftBottom.x - (bottomSurfaceMovePath * shimmerAnimationPercentage),
                    bigShimmerBottomPlane.leftBottom.y
                )
            )
        )

        smallShimmerTopPlane.resetPathWithNewPoints(
            listOf(
                PointF(
                    smallShimmerTopPlane.leftTop.x - (centerSurfaceMovePath * shimmerAnimationPercentage),
                    bigShimmerTopPlane.leftTop.y
                ),
                PointF(
                    smallShimmerTopPlane.rightTop.x - (centerSurfaceMovePath * shimmerAnimationPercentage),
                    smallShimmerTopPlane.rightTop.y
                ),
                smallShimmerTopPlane.rightBottom,
                smallShimmerTopPlane.leftBottom,
            )
        )
        smallShimmerBottomPlane.resetPathWithNewPoints(
            listOf(
                smallShimmerBottomPlane.leftTop,
                smallShimmerBottomPlane.rightTop,
                PointF(
                    smallShimmerBottomPlane.rightBottom.x - (bottomSurfaceMovePath * shimmerAnimationPercentage),
                    smallShimmerBottomPlane.rightBottom.y
                ),
                PointF(
                    smallShimmerBottomPlane.leftBottom.x - (bottomSurfaceMovePath * shimmerAnimationPercentage),
                    smallShimmerBottomPlane.leftBottom.y
                )
            )
        )
    }

    companion object {
        val DEFAULT_SHIMMER_WIDTH = 50.dp
        const val SHIMMER_COLOR = Color.WHITE
        const val STROKE_COLOR = Color.WHITE
        const val SHIMMER_ANIMATION_TIME = 1500
        const val BIG_SHIMMER_WIDTh_PERCENTAGE = 0.55
        const val SMALL_SHIMMER_WIDTH_PERCENTAGE = 0.24
        const val SHIMMER_SPACING_PERCENTAGE = 0.12
        const val DEFAULT_SHADOW_COLOR = Color.BLACK
        const val DEFAULT_SURFACE_COLOR = Color.TRANSPARENT
        val DEFAULT_SHADOW_WIDTH: Float = 3f.dp
        val DEFAULT_STROKE_WIDTH: Float = 1f.dp
        const val ANIMATE_ON_TOUCH: Boolean = false
        const val DEFAULT_CENTER_SURFACE_ROTATION = 18.8f
        const val DEFAULT_BOTTOM_SURFACE_ROTATION = 32f
    }
}
