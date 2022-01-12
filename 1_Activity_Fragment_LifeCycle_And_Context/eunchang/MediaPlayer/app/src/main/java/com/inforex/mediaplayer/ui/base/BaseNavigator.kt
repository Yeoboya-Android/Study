package com.inforex.mediaplayer.ui.base

interface BaseNavigator {
    fun gotoLocalMediaListFragment(showFragment: Boolean)
    fun gotoTapeMediaListFragment(showFragment: Boolean)
    fun showFloatingFragment(showFragment: Boolean)
}