package github.sun5066.lifecycle.ui.activity

import com.bumptech.glide.Glide
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    override fun initBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
    }

    override fun initView() {
        val imageData = intent.getParcelableExtra<ImageData>("imageData")!!

        Glide
            .with(this)
            .load(imageData.uri)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imageView)
    }
}