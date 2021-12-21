package github.sun5066.lifecycle.ui

import android.view.View
import androidx.fragment.app.viewModels
import github.sun5066.lifecycle.databinding.FragmentDetailBinding
import github.sun5066.lifecycle.viewmodel.DetailViewModel
import github.sun5066.lifecycle.viewmodel.MainViewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    /* SharedViewModel */
    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun initViews(view: View) {

    }

    override fun fetchObservables() {

    }
}