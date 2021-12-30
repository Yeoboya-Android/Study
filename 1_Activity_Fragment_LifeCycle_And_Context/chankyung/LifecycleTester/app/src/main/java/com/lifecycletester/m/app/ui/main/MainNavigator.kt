package com.lifecycletester.m.app

import com.lifecycletester.m.app.base.BaseNavigator

interface MainNavigator : BaseNavigator {
    fun createGpsFragment()
    fun replaceGpsFragment()

    fun createChatFragment()
    fun replaceChatFragment()

    fun createSubActivity()
    fun removeFragment()

    fun finishActivity()
}