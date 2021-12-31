package com.inforex.mediaplayer.ui

import android.content.Context
import com.inforex.mediaplayer.ui.base.BaseViewModel

class LocalMediaViewModel : BaseViewModel() {

    private lateinit var mContext: Context

    fun init(applicationContext: Context) {
        mContext = applicationContext
    }
}
