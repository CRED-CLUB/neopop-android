package club.cred.sample.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import club.cred.sample.databinding.ItemSwitchesScreenBinding

class SwitchesScreenViewHolder(parent: ViewGroup, private val itemClickListener: INavigator) :
    RecyclerView.ViewHolder(
        ItemSwitchesScreenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {
    private val binding = ItemSwitchesScreenBinding.bind(itemView)

    init {
        binding.controller.rightBtn.isGone = true
        binding.controller.leftBtn.setOnClickListener {
            itemClickListener.onPreviousPageClicked()
        }
        binding.controller.cancelBtn.setOnClickListener {
            itemClickListener.onCloseClicked()
        }
        var isChecked = false
        binding.radioBtn.setOnClickListener {
            isChecked = !isChecked
            binding.radioBtn.isChecked = isChecked
        }
    }
}
