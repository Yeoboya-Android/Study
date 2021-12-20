package com.lifecycletester.m.app

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.lifecycletester.m.app.util.ApplicationLifecycleObserver
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class MainApplication : MultiDexApplication()
{
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(object : AndroidLogAdapter(){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
    }
}