package github.sun5066.lifecycle.ui

import android.view.View
import androidx.fragment.app.viewModels
import github.sun5066.lifecycle.databinding.FragmentListBinding
import github.sun5066.lifecycle.viewmodel.ListViewModel
import github.sun5066.lifecycle.viewmodel.MainViewModel

class ListFragment : BaseFragment<FragmentListBinding>() {

    /* SharedViewModel */
    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val listViewModel by viewModels<ListViewModel>()

    override fun initViews(view: View) {

    }

    override fun fetchObservables() {

    }
}