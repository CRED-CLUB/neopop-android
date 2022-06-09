package club.cred.sample.viewHolders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScreensAdapter(private val itemClickListener: INavigator) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun getView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> TiltButtonsViewHolder(parent, itemClickListener)
            1 -> PopButtonsViewHolder(parent, itemClickListener)
            2 -> AdvancedScreenViewHolder(parent, itemClickListener)
            3 -> SwitchesScreenViewHolder(parent, itemClickListener)
            else -> TiltButtonsViewHolder(parent, itemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getView(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getItemViewType(position: Int): Int {
        return position % 4
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}
