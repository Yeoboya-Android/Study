package com.lifecycletester.m.app

import com.lifecycletester.m.app.base.BaseNavigator

interface MainNavigator : BaseNavigator {
    fun removeGpsFragment()
    fun finishActivity()
}