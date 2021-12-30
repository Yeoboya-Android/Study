package com.soo.screenrotate.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object ActivityUtil {

    // Activity FrameLayout에 Fragment add.
    fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
            fragmentManager.beginTransaction().add(frameId, fragment, fragment::class.java.name).apply {
                if(addBackStack) addToBackStack(null)
            }.commitAllowingStateLoss()

    // Activity FrameLayout에 Fragment del.
    fun removeFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment) =
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()

    // 현재 fragment replace
    fun replaceFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
            fragmentManager.beginTransaction().replace(frameId, fragment, fragment::class.java.name).apply {
                if(addBackStack) addToBackStack(null)
            }.commitAllowingStateLoss()

    // tag 로 Fragment 를 찾아 반환
    fun getFragment(fragmentManager: FragmentManager, tag: String): Fragment?
            = fragmentManager.findFragmentByTag(tag)

}