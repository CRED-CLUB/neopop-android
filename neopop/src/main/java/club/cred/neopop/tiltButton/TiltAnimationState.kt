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

class TiltAnimationState(
    /**
     * 0f -> button is on top
     * 1f -> button is on bottom
     */
    var currentProgress: Float = 0f,
    var currentState: State = State.Floating,
    var isButtonGoingUp: Boolean = false,
) {

    val isButtonAtBottom: Boolean
        get() = currentProgress == 1f

    val isButtonAtTop: Boolean
        get() = currentProgress == 0f

    var isButtonGoingDown: Boolean
        get() = !isButtonGoingUp
        set(value) {
            isButtonGoingUp = !value
        }

    fun setNextType(onSpace: Boolean) {
        if (onSpace) {
            isButtonGoingUp = !isButtonGoingUp
        }
    }

    sealed class State {
        object Floating : State()
        object Enabling : State()
        object Touch : State()
    }
}
