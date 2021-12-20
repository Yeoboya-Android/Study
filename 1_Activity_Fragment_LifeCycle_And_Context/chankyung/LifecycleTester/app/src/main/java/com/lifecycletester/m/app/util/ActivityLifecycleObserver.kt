package com.lifecycletester.m.app.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.orhanobut.logger.Logger

class ActivityLifecycleObserver(private val lifecycle: Lifecycle) : LifecycleObserver
{
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated(source: LifecycleOwner) {
        Logger.i("ActivityLifecycleObserver::onCreated()")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Logger.i("ActivityLifecycleObserver::onStart()")

        when
        {
            lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)->{
                Logger.i("currentState is greater or equal to INITIALIZED")
            }
            lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)->{
                Logger.i("currentState is greater or equal to CREATED")
            }
            lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)->{
                Logger.i("currentState is greater or equal to STARTED")
            }
            lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)->{
                Logger.i("currentState is greater or equal to RESUMED")
            }
            lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)->{
                Logger.i("currentState is greater or equal to DESTROYED")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Logger.i("onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Logger.i("onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Logger.i("onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Logger.i("onDestroy")
    }
}