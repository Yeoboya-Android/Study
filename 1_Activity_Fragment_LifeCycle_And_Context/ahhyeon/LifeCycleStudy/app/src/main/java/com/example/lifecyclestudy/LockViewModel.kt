package com.example.lifecyclestudy

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager

class LockViewModel(application: Application) : AndroidViewModel(application) {

    /** 양방향 바인딩으로 EditText의 데이터를 바로 받아 뷰모델에서 활용 */
    val etPw = MutableLiveData<String>()

    /** 비밀번호도 데이터 제어와 관련된 부분이라고 생각해 ViewModel에서 처리
     * AndroidViewModel을 통해 application을 가져와 context가 필요한 부분에 활용
     * */
    fun getPassWord() : Boolean {
        val pref : SharedPreferences = getApplication<Application>().getSharedPreferences("LockScreenPassWord", Context.MODE_PRIVATE)
        val passWord = pref.getString("passWord", "0000")
        return etPw.value == passWord
    }
}