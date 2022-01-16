package github.sun5066.lifecycle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.databinding.ItemImageListBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BindingViewHolder(val binding: ItemImageListBinding) : RecyclerView.ViewHolder(binding.root)

class ImageListAdapter(
    private val context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    imageDataFlow: StateFlow<ImageData?>,
    private val onClick: (ImageData) -> Unit,
) :
    RecyclerView.Adapter<BindingViewHolder>() {

    init {
        imageDataFlow.onEach { imageData ->
            imageData?.apply {
                imageList.add(imageData)
                notifyItemInserted(itemCount - 1)
            }
        }.launchIn(lifecycleScope)
    }

    private val imageList = arrayListOf<ImageData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BindingViewHolder(
            ItemImageListBinding.inflate(LayoutInflater.from(context))
        )

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val imageData = imageList[position]
        val imageView = holder.binding.imageView

        Glide
            .with(context)
            .load(imageData.uri)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)

        imageView.setOnClickListener {
            onClick.invoke(imageData)
        }
    }

    override fun getItemCount() = imageList.size
}