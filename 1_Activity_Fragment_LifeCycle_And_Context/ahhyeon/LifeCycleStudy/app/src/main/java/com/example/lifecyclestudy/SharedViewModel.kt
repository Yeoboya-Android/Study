package com.example.lifecyclestudy

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    /**
     * FirstFragment의 EditText에 적은 내용을 양방향 바인딩을 통해 이 라이브 데이터에 넣고
     * MainActivity와 SecondFragment에서 해당 데이터를 바로 공유
     * */
    val fragContent = MutableLiveData("")

}