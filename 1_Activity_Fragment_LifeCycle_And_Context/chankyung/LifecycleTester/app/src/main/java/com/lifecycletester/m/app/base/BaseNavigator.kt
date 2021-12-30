package com.lifecycletester.m.app.base

import android.content.Context
import com.lifecycletester.m.app.util.OnBackPressedListener

interface BaseNavigator
{
    fun getContext() : Context

    fun finishActivity(_isMain : Boolean= false)
    fun showToast(_msg : String)

    // BackButton
    fun setOnBackButtonTouchListener(listener: OnBackPressedListener)
    fun removeOnBackButtonTouchListener()
    fun onBackPressed()
}