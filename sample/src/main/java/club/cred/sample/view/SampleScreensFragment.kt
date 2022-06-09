package club.cred.sample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import club.cred.sample.R
import club.cred.sample.databinding.FragmentScreensContainerBinding
import club.cred.sample.viewHolders.INavigator
import club.cred.sample.viewHolders.ScreensAdapter

class SampleScreensFragment : DialogFragment(), INavigator {
    override fun getTheme(): Int = R.style.FullScreenDialog
    private var binding: FragmentScreensContainerBinding? = null

    private val adapter by lazy { ScreensAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScreensContainerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentPage = requireArguments().getInt("current_page")
        binding?.viewPager?.adapter = adapter
        binding?.viewPager?.setCurrentItem(currentPage, false)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onNextPageClicked() {
        binding?.viewPager?.currentItem = binding?.viewPager?.currentItem?.plus(1) ?: 0
    }

    override fun onPreviousPageClicked() {
        var previousPage = if ((binding?.viewPager?.currentItem?.minus(1) ?: 0) < 0) {
            (binding?.viewPager?.currentItem?.plus(3) ?: 0)
        } else {
            (binding?.viewPager?.currentItem?.minus(1) ?: 0)
        }
        binding?.viewPager?.currentItem = previousPage
    }

    override fun onCloseClicked() {
        dismiss()
    }

    companion object {
        fun show(fragmentManager: FragmentManager, currentPage: Int) {
            SampleScreensFragment().apply {
                arguments = bundleOf("current_page" to currentPage)
                show(fragmentManager, "DemoScreensFragment")
            }
        }
    }
}
