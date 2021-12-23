package com.example.lifecyclestudy

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

enum class ApplicationStatus {
    FOREGROUND, BACKGROUND
}

/** 구글에서 LifecycleObserver를 직접 사용하지 말고 DefaultLifecycleObserver나 LifecycleEventObserver를 사용할 것을 권유
 * 참고) @OnLifecycleEvent(Lifecycle.Event.ON_STOP) 어노테이션 deprecate 됨
 * */
class ApplicationLifeCycleObserver(private val applicationContext : Context) : DefaultLifecycleObserver {

    var appStatus = ApplicationStatus.BACKGROUND

    /** 백/포그라운드에 따라 잠금 액티비티를 보여주기 위해 포그라운드임이 감지 되면 LockActivity 띄우도록 함 */
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        appStatus = ApplicationStatus.FOREGROUND
        LockActivity.startLockActivity(applicationContext)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        appStatus = ApplicationStatus.BACKGROUND
    }
}