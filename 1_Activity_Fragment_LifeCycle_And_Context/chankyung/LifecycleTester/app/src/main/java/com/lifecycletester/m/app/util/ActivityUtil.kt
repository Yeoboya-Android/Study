package com.lifecycletester.m.app.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.orhanobut.logger.Logger

/**********************************************************************************************
 * @class 설명 : Activity Util
 * @작성일  : 2019-12-17
 * @작성자  : hsji
 **********************************************************************************************/
object ActivityUtil {

    private val TAG = ActivityUtil::class.java.simpleName

    /**********************************************************************************************
     * @Method 설명 : Activity FrameLayout에 Fragment add.
     * @작성일  : 2019-12-17
     * @작성자  : hsji
     **********************************************************************************************/
    fun addFragmentToActivity(_fragmentManager: FragmentManager, _fragment: Fragment, _frameId: Int, _tag: String? = null) =
        _tag?.run { _fragmentManager.beginTransaction().add(_frameId, _fragment, _tag).addToBackStack(null).commit() }
            ?: run { _fragmentManager.beginTransaction().add(_frameId, _fragment).addToBackStack(null).commit() }


    /**********************************************************************************************
     * @Method 설명 : Activity FrameLayout에 Fragment del.
     * @작성일  : 2020-02-24
     * @작성자  : DiamondStep
     **********************************************************************************************/
    fun delFragmentToActivity(_fragmentManager: FragmentManager, _fragment: Fragment) =
        _fragmentManager.beginTransaction().remove(_fragment).commit()

    /**********************************************************************************************
     * @Method 설명 : 현재 fragment replace
     * @작성일  : 2019-12-17
     * @작성자  : hsji
     **********************************************************************************************/
    fun replaceFragmentToActivity(_fragmentManager: FragmentManager, _fragment: Fragment, _frameId: Int, _tag: String? = null) =
        _tag?.run {_fragmentManager.beginTransaction().replace(_frameId, _fragment, _tag).addToBackStack(null).commit()}
            ?: run {
                _fragmentManager.beginTransaction().replace(_frameId, _fragment).addToBackStack(null)
                    .commit()
            }

    fun fragmentChangeListener(_fragmentManager: FragmentManager){
        _fragmentManager.addOnBackStackChangedListener {
            Log.i("frag", "change")
        }
    }

    /**
     * isAppRunning을 사용하면 앱이 죽어있을때도, 항상 true 를 반환하게되어
     * 앱이 활성화되었을때 확실히 알 수 있는 메인이 포함되어있는지로 체크...
     * */
    fun isRunningActivity(_context: Context, _activityName : String) :Boolean {
        try {
            val activityManager = _context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
            val listTaskInfo = activityManager.getRunningTasks(9999)
            takeIf { listTaskInfo.size <= 0  }?.let { return false }
            val taskInfo = listTaskInfo[0]
            return taskInfo.baseActivity?.className?.contains(_activityName)!!
        }catch (e : Exception){
            return false
        }
    }

    /**********************************************************************************************
     * @Method 설명 : Top Activity 조회
     * @작성일  : 2019-12-17
     * @작성자  : hsji
     **********************************************************************************************/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getRunningTopActivity(_context: Context?) : String {
        val result = if (_context!== null) {
            val am = _context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) am.getRunningTasks(1)[0].topActivity!!.className
            else am.appTasks[0].taskInfo.topActivity?.className ?: ""
        } else { "" }

        Logger.i("getRunningTopActivity TopActivity: $result")
        return result
    }


    /**
     * 현재 액티비티가 가장 상위에 있는지 체크
     * */
    fun isRunningTopActivity(_context : Context, _activityName : String) :Boolean {
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT){
            if(!getRunningTopActivity(_context).contains(_activityName)) return false
        }
        else{
            if(!isRunningActivity(_context, _activityName)) return false
        }

        return true
    }
}