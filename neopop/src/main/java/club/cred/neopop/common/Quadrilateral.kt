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

package club.cred.neopop.common

import android.graphics.Path
import android.graphics.PointF

// Classes to hold shape related data

data class Quadrilateral(
    val leftTop: PointF,
    val rightTop: PointF,
    val rightBottom: PointF,
    val leftBottom: PointF
) {
    private val path = Path()

    val fullOutline: Path by lazy {
        path.apply {
            moveTo(leftTop.x, leftTop.y)
            lineTo(rightTop.x, rightTop.y)
            lineTo(rightBottom.x, rightBottom.y)
            lineTo(leftBottom.x, leftBottom.y)
            close()
        }
    }

    fun resetPathWithNewPoints(points: List<PointF>) {
        path.apply {
            reset()
            points.forEachIndexed { index, pointF ->
                if (index == 0) {
                    moveTo(pointF.x, pointF.y)
                } else {
                    lineTo(pointF.x, pointF.y)
                }
            }
            close()
        }
    }

    fun translate(x: Float, y: Float) = Quadrilateral(
        leftTop = PointF(leftTop.x + x, leftTop.y + y),
        rightTop = PointF(rightTop.x + x, rightTop.y + y),
        rightBottom = PointF(rightBottom.x + x, rightBottom.y + y),
        leftBottom = PointF(leftBottom.x + x, leftBottom.y + y),
    )
}

data class Polygon(
    val points: List<PointF>
) {
    private val path = Path()
    val fullOutline: Path by lazy {
        path.apply {
            points.forEachIndexed { index, pointF ->
                if (index == 0) {
                    moveTo(pointF.x, pointF.y)
                } else {
                    lineTo(pointF.x, pointF.y)
                }
            }
            close()
        }
    }

    fun resetPathWithNewPoints(points: List<PointF>) {
        path.apply {
            reset()
            points.forEachIndexed { index, pointF ->
                if (index == 0) {
                    moveTo(pointF.x, pointF.y)
                } else {
                    lineTo(pointF.x, pointF.y)
                }
            }
            close()
        }
    }
}

data class OpenPolygon(
    val points: List<PointF>
) {
    private val path = Path()
    val fullOutline: Path by lazy {
        path.apply {
            points.forEachIndexed { index, pointF ->
                if (index == 0) {
                    moveTo(pointF.x, pointF.y)
                } else {
                    lineTo(pointF.x, pointF.y)
                }
            }
        }
    }

    fun resetPathWithNewPoints(points: List<PointF>) {
        path.apply {
            reset()
            points.forEachIndexed { index, pointF ->
                if (index == 0) {
                    moveTo(pointF.x, pointF.y)
                } else {
                    lineTo(pointF.x, pointF.y)
                }
            }
        }
    }
}
