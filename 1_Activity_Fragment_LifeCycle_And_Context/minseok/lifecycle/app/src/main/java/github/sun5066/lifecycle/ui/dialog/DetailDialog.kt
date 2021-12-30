package github.sun5066.lifecycle.ui.dialog

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.databinding.DialogDetailBinding
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.viewmodel.MainViewModel

class DetailDialog : DialogFragment() {

    private val binding by lazy { DialogDetailBinding.inflate(LayoutInflater.from(requireContext())) }
    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireActivity() })
    private lateinit var imageData: ImageData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageData = requireArguments().get("imageData") as ImageData
        val imageView = binding.imageView

        Glide
            .with(requireContext())
            .load(imageData.uri)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)

        dialog?.window?.apply {
            setLayout(300.toDp(), 500.toDp())
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setLastViewState(LastViewState.DetailDialog(imageData))
    }
}

fun Int.toDp() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()