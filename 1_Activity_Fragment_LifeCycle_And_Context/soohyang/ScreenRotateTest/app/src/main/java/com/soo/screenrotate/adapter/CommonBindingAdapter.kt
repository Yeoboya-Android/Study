package com.soo.screenrotate.adapter

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

@BindingAdapter("clickListener")
fun bindClickListener(view: View, clickListener: View.OnClickListener?) {
    view.setOnClickListener(clickListener)
}

@BindingAdapter(value = ["visibility", "goneEnable"], requireAll = false)
fun setVisibility(view: View, visibility: Boolean?, goneEnable: Boolean?){
    val isVisibility = visibility ?: true
    val isGoneEnable = goneEnable ?: false

    view.visibility = when {
        isVisibility -> View.VISIBLE
        isGoneEnable -> View.GONE
        else -> View.INVISIBLE
    }
}

@BindingAdapter("textStringResId")
fun setTextRes(view: TextView, @StringRes messageRes: Int?) {
    messageRes?.takeIf { it > -1 }?.also { view.setText(it) }
}