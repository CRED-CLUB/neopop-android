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
import club.cred.neopop.common.dp

data class TiltFrameLayoutStyle(
    val centerSurfaceColor: Int = 0,
    val bottomSurfaceColor: Int = Color.TRANSPARENT,
    val depth: Float = TiltGeometry.DEFAULT_SHADOW_WIDTH,
    val centerSurfaceRotation: Float = 0.dp,
    val bottomSurfaceRotation: Float = 30f,
    val gravity: NeoPopGravity = NeoPopGravity.ON_SPACE,
    val shimmerColor: Int = TiltGeometry.SHIMMER_COLOR,
    val shimmerWidth: Float = TiltGeometry.DEFAULT_SHIMMER_WIDTH,
    val shimmerAnimationTime: Int = TiltGeometry.SHIMMER_ANIMATION_TIME,
    val hasShimmer: Boolean = true,
    val shadowHeight: Float = 0.dp,
    val bottomShimmerColor: Int = TiltGeometry.SHIMMER_COLOR,
    val isEnabled: Boolean = true,
    val shimmerStartDelay: Long = 0L,
    val shimmerRepeatDelay: Long = 0L,
    val shadowColor: Int = TiltGeometry.DEFAULT_FLOATING_SHADOW_COLOR,
    val strokeColor: Int = TiltGeometry.STROKE_COLOR,
    val isStrokeEnabled: Boolean = false,
    val strokeWidth: Float = TiltGeometry.DEFAULT_STROKE_WIDTH,
    val animateOnTouch: Boolean = TiltGeometry.ANIMATE_ON_TOUCH
) {

    companion object {
        fun getDisabledStateStyle(neoPopStyle: TiltFrameLayoutStyle): TiltFrameLayoutStyle {
            return neoPopStyle.copy(
                centerSurfaceColor = 0xff8A8A8A.toInt(),
                bottomSurfaceColor = 0xffB6B6B6.toInt(),
                isEnabled = false
            )
        }

        fun createDisableStyle(
            neoPopStyle: TiltFrameLayoutStyle,
            cardColor: Int,
            shadowColor: Int
        ): TiltFrameLayoutStyle {
            return neoPopStyle.copy(
                centerSurfaceColor = cardColor,
                bottomSurfaceColor = shadowColor,
                isEnabled = false
            )
        }
    }
}

enum class NeoPopGravity(val value: Int) {
    ON_SPACE(0),
    ON_GROUND(1);

    companion object {
        fun getValueFromInt(value: Int): NeoPopGravity {
            return if (value == ON_GROUND.value) {
                ON_GROUND
            } else {
                ON_SPACE
            }
        }
    }
}
