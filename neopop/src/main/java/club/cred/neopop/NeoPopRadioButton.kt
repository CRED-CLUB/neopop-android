package club.cred.neopop

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatRadioButton
import club.cred.neopop.common.NeoPopCheckable
import club.cred.neopop.common.createCheckableSelector
import club.cred.neopop.common.withAttrs
import com.dreamplug.neopop.R

class NeoPopRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatRadioButton(context, attrs), NeoPopCheckable {

    init {
        withAttrs(attrs, R.styleable.NeoPopRadioButton) {
            val enabledDrawable = getDrawable(
                R.styleable.NeoPopRadioButton_checked_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_radio_button_selected)!!

            val disableDrawable = getDrawable(
                R.styleable.NeoPopRadioButton_un_checked_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_radio_button_unselected)!!

            buttonDrawable = createCheckableSelector(enabledDrawable, disableDrawable)
        }
    }
}
