package com.lifecycletester.m.app.ui.fragment.chat

import androidx.databinding.ObservableField

class ItemChatMsgViewModel
{
    val msg = ObservableField("")

    fun initData(_msg : String){
        msg.set(_msg)
    }
}