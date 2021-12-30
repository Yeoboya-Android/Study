package com.soo.screenrotate.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.soo.screenrotate.R
import com.soo.screenrotate.utils.CommonUtils
import com.soo.screenrotate.utils.CommonUtils.combine

class LiveViewModel : ViewModel() {

    private val _isBroadcaster = MutableLiveData<Boolean>(false)
    val isBroadcaster : LiveData<Boolean> = _isBroadcaster

    private val _isDetectFace = MutableLiveData<Boolean>(false)
    val showWarningDetect : LiveData<Boolean> = Transformations.map(_isBroadcaster.combine(_isDetectFace)) {
        it.first == true && it.second == false
    }

    private val _detectFaceStringResId = MutableLiveData<Int>(R.string.toast_not_detect_face)
    val detectFaceStringResId : LiveData<Int> = _detectFaceStringResId

    fun setIsBroadcaster(flag: Boolean) {
        _isBroadcaster.postValue(flag)
    }

    fun setDetectFace(faceDetect: Boolean, @StringRes description: Int) {
        _isDetectFace.postValue(faceDetect)
        _detectFaceStringResId.postValue(description)
    }
}