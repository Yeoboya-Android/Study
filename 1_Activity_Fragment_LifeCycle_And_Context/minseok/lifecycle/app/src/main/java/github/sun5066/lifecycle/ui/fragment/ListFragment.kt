package github.sun5066.lifecycle.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import github.sun5066.lifecycle.databinding.FragmentListBinding
import github.sun5066.lifecycle.ui.activity.DetailActivity
import github.sun5066.lifecycle.ui.activity.MainActivity
import github.sun5066.lifecycle.ui.adapter.ImageListAdapter
import github.sun5066.lifecycle.ui.dialog.DetailDialog
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.ui.state.LifeCycleModeState
import github.sun5066.lifecycle.util.autoCleared
import github.sun5066.lifecycle.viewmodel.ListViewModel
import github.sun5066.lifecycle.viewmodel.MainViewModel

class ListFragment : BaseFragment<FragmentListBinding>() {

    /* SharedViewModel */
    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val listViewModel by viewModels<ListViewModel>()

    private var imageListAdapter by autoCleared<ImageListAdapter>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initAdapters() {
        imageListAdapter = ImageListAdapter(requireContext()) { imageData ->
            when (mainViewModel.lifeCycleState.value) {
                LifeCycleModeState.Default, LifeCycleModeState.BackStack -> {
                    (requireActivity() as MainActivity).showDetailFragment(imageData)
                }
                LifeCycleModeState.Dialog -> {
                    DetailDialog().apply {
                        arguments = bundleOf(Pair("imageData", imageData))
                        show(childFragmentManager, "DetailDialog")
                    }
                }
                LifeCycleModeState.NewActivity -> {
                    startActivity(
                        Intent(requireContext(), DetailActivity::class.java).apply {
                            putExtra("imageData", imageData)
                        }
                    )
                }
                null -> throw NullPointerException("상태가 Null 입니다!")
            }
        }
    }

    override fun initViews(view: View) {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = imageListAdapter
        }
    }

    override fun fetchObservables() {
        listViewModel.imageList.observe(viewLifecycleOwner) { imageList ->
            imageListAdapter.notifyData(imageList)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setLastViewState(LastViewState.ListFragment)
    }
}
