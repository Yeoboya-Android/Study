package com.example.networkstatemonitortest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    // 데이터 바인딩을 통한 xml에서 String Format 사용하기
    private var _networkStatus: MutableLiveData<String> = MutableLiveData("")
    val networkStatus: LiveData<String> get() = _networkStatus

    fun setNetworkStatus(isConnected: Boolean) {
        _networkStatus.value = if (isConnected) "연결" else "해제"
    }

}