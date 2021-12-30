package com.lifecycletester.m.app.ui.fragment.chat

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lifecycletester.m.app.databinding.ItemChatMsgBinding

class ChatViewHolder(context : Context, val binding : View) : RecyclerView.ViewHolder(binding)
{
    val mBinding : ItemChatMsgBinding? = DataBindingUtil.bind(binding)
}