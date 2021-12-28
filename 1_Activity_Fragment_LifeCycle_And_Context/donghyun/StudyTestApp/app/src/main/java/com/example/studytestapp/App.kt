package com.example.studytestapp

import android.app.Application
import androidx.lifecycle.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(ListenerBackground())
    }
}