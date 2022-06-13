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

class NeoPopAnimationState(
    var isCompleted: Boolean = false,
    var isFloating: Boolean = true,
    var isButtonGoingUp: Boolean = false
) {
    /**
     * 0f -> button is on top
     * 1f -> button is on bottom
     */
    var currentProgress: Float = 0f

    val isButtonAtBottom: Boolean
        get() = currentProgress == 1f

    val isButtonAtTop: Boolean
        get() = currentProgress == 0f

    val isButtonGoingDown: Boolean
        get() = !isButtonGoingUp

    fun setNextType(onSpace: Boolean) {
        if (onSpace) {
            when (isButtonGoingUp) {
                true -> {
                    isButtonGoingUp = false
                    isFloating = true
                }
                false -> {
                    isButtonGoingUp = true
                }
            }
        }
    }

    fun setButtonDown() {
        isButtonGoingUp = false
        isFloating = false
    }

    fun setButtonUp() {
        isButtonGoingUp = true
        isFloating = false
    }
}
