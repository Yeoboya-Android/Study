package com.inforex.mediaplayer.util

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class DataBindingViewHolder<T : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: T? = DataBindingUtil.bind(itemView)
}