package club.cred.neopop

import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SwitchCompat
import club.cred.neopop.common.NeoPopCheckable
import club.cred.neopop.common.createCheckableSelector
import club.cred.neopop.common.withAttrs
import com.dreamplug.neopop.R

class NeoPopToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), NeoPopCheckable {

    private var checkedChangeListener: NeoPopCheckable.OnCheckedChangeListener? = null
    private var switch: SwitchCompat = SwitchCompat(context)
    private var hapticsEnabled: Boolean = true

    init {
        withAttrs(attrs, R.styleable.NeoPopToggleButton) {
            val checkedDrawable = getDrawable(
                R.styleable.NeoPopToggleButton_checked_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_switch_thumb_true)!!

            val unCheckedDrawable = getDrawable(
                R.styleable.NeoPopToggleButton_un_checked_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_switch_thumb_false)!!

            val trackDrawable = getDrawable(
                R.styleable.NeoPopToggleButton_track_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_track_colored)!!

            hapticsEnabled = getBoolean(R.styleable.NeoPopToggleButton_toggleHapticsEnabled, true)

            switch.thumbDrawable = createCheckableSelector(checkedDrawable, unCheckedDrawable)
            switch.trackDrawable = trackDrawable
        }
        addView(switch)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (hapticsEnabled) {
                switch.performHapticFeedback(
                    HapticFeedbackConstants.KEYBOARD_TAP,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
            }
            checkedChangeListener?.onCheckedChanged(this@NeoPopToggleButton, isChecked = isChecked)
        }
    }

    override fun setChecked(checked: Boolean) {
        if (switch.isChecked == checked) return
        switch.isChecked = checked
    }

    override fun isChecked(): Boolean {
        return switch.isChecked
    }

    override fun toggle() {
        switch.toggle()
    }

    fun setOnCheckedChangeListener(checkedChangeListener: NeoPopCheckable.OnCheckedChangeListener?) {
        this.checkedChangeListener = checkedChangeListener
    }

    override fun setEnabled(enabled: Boolean) {
        switch.isEnabled = enabled
        super.setEnabled(enabled)
    }
}
