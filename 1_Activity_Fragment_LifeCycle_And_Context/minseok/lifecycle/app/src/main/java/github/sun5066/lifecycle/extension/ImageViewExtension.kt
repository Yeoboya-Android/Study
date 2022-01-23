package github.sun5066.lifecycle.extension

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.centerCrop(url: Uri, @DrawableRes placeholder: Int, @DrawableRes error: Int) {
    Glide
        .with(context)
        .load(url)
        .centerCrop()
        .placeholder(placeholder)
        .error(error)
        .into(this)
}