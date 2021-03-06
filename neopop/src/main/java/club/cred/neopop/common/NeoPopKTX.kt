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

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

inline fun Canvas.translateWith(dx: Float, dy: Float, block: () -> Unit) {
    translate(dx, dy)
    block()
    translate(-dx, -dy)
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
