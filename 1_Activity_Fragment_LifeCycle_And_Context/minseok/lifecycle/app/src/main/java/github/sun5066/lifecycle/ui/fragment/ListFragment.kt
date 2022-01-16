package github.sun5066.lifecycle.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import github.sun5066.lifecycle.databinding.FragmentListBinding
import github.sun5066.lifecycle.ui.activity.*
import github.sun5066.lifecycle.ui.adapter.ImageListAdapter
import github.sun5066.lifecycle.ui.dialog.DetailDialog
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.ui.state.LifeCycleModeState
import github.sun5066.lifecycle.viewmodel.ListViewModel
import github.sun5066.lifecycle.viewmodel.MainViewModel

class ListFragment : BaseFragment<FragmentListBinding>() {

    /* SharedViewModel */
    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val listViewModel by viewModels<ListViewModel>()

    private val imageListAdapter by lazy { makeAdapter() }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViews(view: View) {
        requireActivity().setStatusBarTransparent()

        binding.content.updatePadding(
            bottom = requireContext().navigationHeight()
        )

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = imageListAdapter
        }
    }

    override fun fetchData() = Unit

    override fun onResume() {
        super.onResume()
        mainViewModel.setLastViewState(LastViewState.ListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().setStatusBarOrigin()
    }

    private fun makeAdapter() =
        ImageListAdapter(requireContext(), lifecycleScope, listViewModel.imageData) { selectImage ->
            when (mainViewModel.lifeCycleState.value) {
                LifeCycleModeState.Default, LifeCycleModeState.BackStack -> {
                    (requireActivity() as MainActivity).showDetailFragment(selectImage)
                }
                LifeCycleModeState.Dialog -> {
                    DetailDialog().apply {
                        arguments = bundleOf(Pair("imageData", selectImage))
                    }.show(childFragmentManager, "DetailDialog")
                }
                LifeCycleModeState.NewActivity -> {
                    startActivity(
                        Intent(requireContext(), DetailActivity::class.java).apply {
                            putExtra("imageData", selectImage)
                        }
                    )
                }
                else -> throw IllegalStateException("유효한 상태가 아닙니다!")
            }
        }
}
