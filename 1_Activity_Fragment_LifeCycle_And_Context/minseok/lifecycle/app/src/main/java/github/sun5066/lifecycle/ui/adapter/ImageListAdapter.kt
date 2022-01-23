package github.sun5066.lifecycle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.databinding.ItemImageListBinding
import github.sun5066.lifecycle.extension.centerCrop

class BindingViewHolder(val binding: ItemImageListBinding) : RecyclerView.ViewHolder(binding.root)

class ImageListAdapter(
    private val context: Context,
    private val onClick: (ImageData) -> Unit,
) : RecyclerView.Adapter<BindingViewHolder>() {

    private val imageList = arrayListOf<ImageData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BindingViewHolder(
            ItemImageListBinding.inflate(LayoutInflater.from(context))
        )

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) =
        with(holder.binding.imageView) {
            val imageData = imageList[position]
            centerCrop(
                imageData.uri,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background
            )
            setOnClickListener {
                onClick.invoke(imageData)
            }
        }

    override fun getItemCount() = imageList.size

    fun submit(list: List<ImageData>) {
        val position = itemCount
        (position until list.size).forEach { i ->
            imageList.add(list[i])
            notifyItemInserted(i)
        }
    }
}