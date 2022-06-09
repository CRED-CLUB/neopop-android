package club.cred.neopop.common

import android.graphics.Path
import android.graphics.PointF

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
