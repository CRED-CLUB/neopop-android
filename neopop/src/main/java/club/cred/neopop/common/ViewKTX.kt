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

import android.content.Context
import android.content.res.TypedArray
import android.media.AudioManager
import android.provider.Settings
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View

internal fun View.shouldPerformHaptics(checkForSilentMode: Boolean = false): Boolean {
    val isSilent = checkForSilentMode &&
        (context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager)?.ringerMode == AudioManager.RINGER_MODE_SILENT

    val isVibrateOnTouchEnabled = Settings.System.getInt(
        context.contentResolver,
        Settings.System.HAPTIC_FEEDBACK_ENABLED, 1
    ) != 0

    return isHapticFeedbackEnabled && isVibrateOnTouchEnabled && !isSilent
}

internal fun View.provideSafeHapticFeedback() {
    if (shouldPerformHaptics(true)) {
        performHapticFeedback(
            HapticFeedbackConstants.KEYBOARD_PRESS,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    } else {
        return
    }
}

internal inline fun View.withAttrs(
    set: AttributeSet?,
    attrs: IntArray,
    func: TypedArray.() -> Unit
) {
    val a = context.theme.obtainStyledAttributes(set, attrs, 0, 0)
    try {
        a.func()
    } finally {
        a.recycle()
    }
}

internal fun View.isEventWithinBounds(event: MotionEvent): Boolean =
    event.x in 0f..width.toFloat() && event.y in 0f..height.toFloat()
