package club.cred.neopop.common

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import android.view.HapticFeedbackConstants
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
