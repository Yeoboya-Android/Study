package com.example.studytestapp.mypage

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studytestapp.BaseViewModel
import com.example.studytestapp.R
import com.example.studytestapp.widget.MaxLineEditText
import com.example.studytestapp.widget.StringUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// fragment 가 detached 되기 전까지, activiy 가 finish 되기 전까지 존재
class MyPageViewModel(application: Application) : BaseViewModel(application) {
    private val TEXT_MIN_LENGTH = 5
    val CONTS_MAX_LENGTH = 20

    private lateinit var mauthListener: AuthListener
    private val mContext: Context get() = getApplication<Application>().applicationContext

    private var _clearFocus = MutableLiveData(false) // EditText 포커스 없애기 위한 flag
    val clearFocus: LiveData<Boolean> get() = _clearFocus

    private var _isEnabled = MutableLiveData(true)
    val isEnabled: LiveData<Boolean> get() = _isEnabled

    private val _id = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _signInOutText = MutableLiveData<String>("로그인")

    val id: LiveData<String> = _id
    val password: LiveData<String> = _password
    val signInOutText: LiveData<String> = _signInOutText

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    val clickListener = View.OnClickListener {
        when(it.id) {
            R.id.signUpButton -> signUpButton()
            R.id.signInOutButton -> signInOutButton()
        }
        _clearFocus.value = true
    }

    fun init(authListener: AuthListener) {
        mauthListener = authListener
    }

    private fun signUpButton() {
        val email = _id.value.toString()
        val password = _password.value.toString()


        mauthListener.authSignUpListener(auth, email, password)
    }

    private fun signInOutButton() {
        val email = _id.value.toString()
        val password = _password.value.toString()

        if(auth.currentUser == null) {
            // 로그인
            mauthListener.authListener(auth, email, password)
            _isEnabled.value = false
        } else {
            // 로그아웃
            _isEnabled.value = true
            setLogOut()
        }
    }


    fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(mContext, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        setLogIn()
    }

    val changeEditTextListener = object : MaxLineEditText.ChangeEditTextListener {
        override fun onTextChange(view: MaxLineEditText, text: String, textLength: Int) {
            when(view.id) {
               R.id.emailEditText -> {
                   _id.value = text
               }
               R.id.passwordEditText -> {
                    _password.value = text
               }
            }
        }

        override fun onOverTextLimit(view: MaxLineEditText) {}

        override fun onFocusChange(view: MaxLineEditText, focus: Boolean) {
            val text = view.text.toString()
            Log.d("로그","onFocusChange-text : $text")
            if (!focus && StringUtil.getGraphemeLength(text) < TEXT_MIN_LENGTH)
                Log.d("로그","MiN에러")
        }
    }

    private fun setLogOut() {
        auth.signOut()
        _id.value = ""
        _password.value = ""
        _signInOutText.value = "로그인"
    }

    private fun setLogIn() {
        _signInOutText.value = "로그아웃"
        _id.value = auth.currentUser.email
        _password.value = "*************"
        _isEnabled.value = false
    }
}