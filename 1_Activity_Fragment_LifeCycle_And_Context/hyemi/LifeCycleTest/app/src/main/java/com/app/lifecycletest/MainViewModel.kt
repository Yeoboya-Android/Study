package com.app.lifecycletest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _click = MutableLiveData<Int>(0)
    val click : LiveData<Int> get() = _click                    // 만나고 싶어요 버튼 리소스


    fun add() {
        _click.value?.let {
            _click.postValue(it.plus(1))
        }
    }
}