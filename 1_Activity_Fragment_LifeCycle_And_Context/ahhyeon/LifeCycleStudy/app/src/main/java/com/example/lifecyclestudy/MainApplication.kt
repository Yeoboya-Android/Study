package com.example.lifecyclestudy

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifeCycleObserver(applicationContext))
    }
}