package com.inforex.mediaplayer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


object ImageUtil {


    // RequestBuilder<Drawable>
    private fun getRequestDrawableBuilder(context: Context, url: Any, diskCacheFlag: DiskCacheStrategy = DiskCacheStrategy.RESOURCE) =
        Glide.with(context).load(url).apply(RequestOptions()).diskCacheStrategy(diskCacheFlag)

    // RequestBuilder<Bitmap>
    private fun getRequestBitmapBuilder(context: Context, url: Any, diskCacheFlag: DiskCacheStrategy = DiskCacheStrategy.RESOURCE) =
        Glide.with(context).asBitmap().load(url).apply(RequestOptions()).diskCacheStrategy(diskCacheFlag)


    /** ImageView 이미지 세팅 */
    // ImageView set ImgResId
    fun setImage(context: Context, imageView: ImageView, resID: Int, diskCacheFlag : DiskCacheStrategy = DiskCacheStrategy.RESOURCE) =
        resID.takeIf { it > 0 }?.run { Glide.with(context).load(resID).diskCacheStrategy(diskCacheFlag).into(imageView) }

    // ImageView set Image Uri
    fun setImage(context: Context, imageView: ImageView, uri: Uri, diskCacheFlag : DiskCacheStrategy = DiskCacheStrategy.RESOURCE, defaultResId: Int = 0) =
        Glide.with(context).load(uri).diskCacheStrategy(diskCacheFlag).placeholder(defaultResId).into(imageView)

    // ImageView set Image Url
    fun setImage(context: Context, imageView: ImageView, url: String, defaultResId: Int = 0, diskCacheFlag: DiskCacheStrategy = DiskCacheStrategy.RESOURCE) {
        val manager = getRequestDrawableBuilder(context, url, diskCacheFlag)
            .placeholder(defaultResId)
            .error(defaultResId)
        imageView.layoutParams?.let { manager.override(it.width, it.height) }

        manager.into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    manager.into(imageView)
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // 로드 실패 시 캐시파일 로드하지 않고 직접 재호출
                    setImage(context, imageView, defaultResId)
                }
            })
    }
}