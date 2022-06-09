package club.cred.neopop.common

import android.view.View
import android.widget.Checkable

interface NeoPopCheckable : Checkable {

    fun interface OnCheckedChangeListener {
        fun onCheckedChanged(view: View, isChecked: Boolean)
    }
}
