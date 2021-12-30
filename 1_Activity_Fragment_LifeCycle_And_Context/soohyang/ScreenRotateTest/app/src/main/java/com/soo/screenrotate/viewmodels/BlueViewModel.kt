package com.soo.screenrotate.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class BlueViewModel : ViewModel() {

    private val _count = MutableLiveData<Int>(0)
    val count : LiveData<String> = Transformations.map(_count) {
        it.toString()
    }


    fun addCount() {
        val currentCount = _count.value
        if (currentCount != null) {
            _count.value = currentCount + 1
        }
    }
}