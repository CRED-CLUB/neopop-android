package club.cred.sample.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import club.cred.sample.R
import club.cred.sample.databinding.FragmentNavigatorBinding

class NavigatorFragment : Fragment(R.layout.fragment_navigator) {

    var binding: FragmentNavigatorBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentNavigatorBinding.bind(requireView())
        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding!!) {
            mainBtn.root.setOnClickListener {
                SampleScreensFragment.show(childFragmentManager, 0)
            }
            primaryBtn.root.setOnClickListener {
                SampleScreensFragment.show(childFragmentManager, 1)
            }
            advBtn.root.setOnClickListener {
                SampleScreensFragment.show(childFragmentManager, 2)
            }
            switchBtn.root.setOnClickListener {
                SampleScreensFragment.show(childFragmentManager, 3)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
