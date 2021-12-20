package com.lifecycletester.m.app.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.orhanobut.logger.Logger

class ApplicationLifecycleObserver : LifecycleObserver
{
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground(){
        Logger.i("ApplicationLifecycleObserver::onForeground()")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground(){
        Logger.i("ApplicationLifecycleObserver::onBackground()")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed(){
        Logger.i("ApplicationLifecycleObserver::onDestroyed()")
    }
}