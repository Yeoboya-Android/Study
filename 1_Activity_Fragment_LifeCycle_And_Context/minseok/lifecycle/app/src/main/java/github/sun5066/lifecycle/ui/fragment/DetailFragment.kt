package github.sun5066.lifecycle.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.databinding.FragmentDetailBinding
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.viewmodel.DetailViewModel
import github.sun5066.lifecycle.viewmodel.MainViewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    /* SharedViewModel */
    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViews(view: View) {
        val imageData = requireArguments().get("imageData") as ImageData
        detailViewModel.setImageData(imageData)
    }

    override fun fetchData() {
        detailViewModel.imageData.observe(viewLifecycleOwner) { imageData ->
            Glide
                .with(requireContext())
                .load(imageData.uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.imageView)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setLastViewState(LastViewState.DetailFragment(detailViewModel.imageData.value!!))
    }
}