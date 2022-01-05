@file:Suppress("LABEL_NAME_CLASH")

package com.inforex.mediaplayer.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**********************************************************************************************
 * @class 설명 : Activity Util
 * @작성일  : 2019-12-17
 * @작성자  : hsji
 **********************************************************************************************/
object ActivityUtil {


    // Activity FrameLayout에 Fragment add.
    fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
        fragmentManager.beginTransaction().add(frameId, fragment, fragment::class.java.name).apply {
            if (addBackStack) addToBackStack(null)
        }.commitAllowingStateLoss()


    // Activity FrameLayout에 Fragment del.
    fun removeFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment) =
        fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()

    // 현재 fragment replace
    fun replaceFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
        fragmentManager.beginTransaction().replace(frameId, fragment, fragment::class.java.name).apply {
            if (addBackStack) addToBackStack(null)
        }.commitAllowingStateLoss()

    // tag 로 Fragment 를 찾아 반환
    fun getFragment(fragmentManager: FragmentManager, tag: String): Fragment?
            = fragmentManager.findFragmentByTag(tag)

    /**
     * isAppRunning을 사용하면 앱이 죽어있을때도, 항상 true 를 반환하게되어
     * 앱이 활성화되었을때 확실히 알 수 있는 메인이 포함되어있는지로 체크...
     * */
    fun isRunningMainActivity(context: Context) =
        try {
            val activityManager = context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
            val listTaskInfo = activityManager.getRunningTasks(9999)
            if (listTaskInfo.size > 0) {
                val taskInfo = listTaskInfo[0]
                taskInfo.baseActivity?.className?.contains("MainActivity")!!
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }

    // Top Activity 조회
    private fun getRunningTopActivity(context: Context) : String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val result = am.appTasks?.get(0)?.taskInfo?.topActivity?.className ?: ""

        return result
    }

    // 현재 액티비티가 가장 상위에 있는지 체크
    fun isRunningTopActivity(context : Context, activityName : String) :Boolean {
        val isRunningMainActivity = isRunningMainActivity(context)

        val isTopActivity = getRunningTopActivity(context).contains(activityName)
        if (!isRunningMainActivity || !isTopActivity)
            return false

        return true
    }

    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        procInfos?.forEach { processInfo ->
            if (processInfo.processName == packageName) {
                return true
            }
        }

        return false
    }
}