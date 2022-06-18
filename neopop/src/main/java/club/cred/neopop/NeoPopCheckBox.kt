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

package club.cred.neopop

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckBox
import club.cred.neopop.common.NeoPopCheckable
import club.cred.neopop.common.createCheckableSelector
import club.cred.neopop.common.withAttrs
import com.dreamplug.neopop.R

class NeoPopCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), NeoPopCheckable {

    private var checkedChangeListener: NeoPopCheckable.OnCheckedChangeListener? = null
    var checkBox: AppCompatCheckBox = AppCompatCheckBox(context)

    init {
        withAttrs(attrs, R.styleable.NeoPopCheckBox) {
            val enabledDrawable = getDrawable(
                R.styleable.NeoPopCheckBox_checked_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_checkbox_selected)!!

            val disableDrawable = getDrawable(
                R.styleable.NeoPopCheckBox_un_checked_drawable
            ) ?: AppCompatResources.getDrawable(context, R.drawable.ic_checkbox_unselected)!!

            checkBox.buttonDrawable = createCheckableSelector(enabledDrawable, disableDrawable)
        }
        checkBox.apply {
            minWidth = 0
            minHeight = 0
            setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener?.onCheckedChanged(this@NeoPopCheckBox, isChecked)
            }
            this@NeoPopCheckBox.addView(this)
        }
    }

    override fun setChecked(checked: Boolean) {
        if (checkBox.isChecked == checked) return
        checkBox.isChecked = checked
    }

    override fun isChecked(): Boolean {
        return checkBox.isChecked
    }

    override fun toggle() {
        checkBox.toggle()
    }

    fun setOnCheckedChangeListener(checkedChangeListener: NeoPopCheckable.OnCheckedChangeListener?) {
        this.checkedChangeListener = checkedChangeListener
    }

    override fun setEnabled(enabled: Boolean) {
        checkBox.isEnabled = enabled
        super.setEnabled(enabled)
    }
}
