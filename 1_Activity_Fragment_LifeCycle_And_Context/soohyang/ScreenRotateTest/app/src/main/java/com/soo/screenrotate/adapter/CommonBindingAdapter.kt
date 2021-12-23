package com.soo.screenrotate.adapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("clickListener")
fun bindClickListener(view: View, clickListener: View.OnClickListener?) {
    view.setOnClickListener(clickListener)
}