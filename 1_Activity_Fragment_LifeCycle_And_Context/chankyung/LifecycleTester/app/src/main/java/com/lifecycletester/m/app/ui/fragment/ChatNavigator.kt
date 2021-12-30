package com.lifecycletester.m.app.ui.fragment

import com.lifecycletester.m.app.base.BaseFragmentNavigator

interface ChatNavigator : BaseFragmentNavigator
{
    fun onChatMsg(_msg : String)
}