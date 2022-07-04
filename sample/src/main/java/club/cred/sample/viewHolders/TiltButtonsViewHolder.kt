package club.cred.sample.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import club.cred.sample.databinding.ItemTiltButtonScreenBinding

class TiltButtonsViewHolder(parent: ViewGroup, private val itemClickListener: INavigator) :
    RecyclerView.ViewHolder(
        ItemTiltButtonScreenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {
    private val binding = ItemTiltButtonScreenBinding.bind(itemView)

    init {
        binding.controller.rightBtn.setOnClickListener {
            itemClickListener.onNextPageClicked()
        }
        binding.controller.cancelBtn.setOnClickListener {
            itemClickListener.onCloseClicked()
        }
        binding.controller.leftBtn.isGone = true
    }
}
