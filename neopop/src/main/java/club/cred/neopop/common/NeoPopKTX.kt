package club.cred.neopop.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

inline fun Canvas.translateWith(dx: Float, dy: Float, block: () -> Unit) {
    translate(dx, dy)
    block()
    translate(-dx, -dy)
}

fun Canvas.clip(path: Path, block: () -> Unit) {
    clipPath(path)
    block()
}

fun Canvas.drawStroke(stroke: Stroke, paint: Paint) {
    this.drawLine(
        stroke.start.x,
        stroke.start.y,
        stroke.end.x,
        stroke.end.y, paint
    )
}

fun Canvas.drawStroke(path: Path, paint: Paint) {
    this.drawPath(path, paint)
}
