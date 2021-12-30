package com.example.testapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class MyObserver(context: Context, lifecycle: Lifecycle): DefaultLifecycleObserver {

        init {
            lifecycle.addObserver(this)
        }

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            Log.d("woong", "onCreate")
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            Log.d("woong", "onResume")
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            Log.d("woong", "onPause")
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            Log.d("woong", "onStart")
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            Log.d("woong", "onStop")
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            Log.d("woong", "onDestroy")
        }

}