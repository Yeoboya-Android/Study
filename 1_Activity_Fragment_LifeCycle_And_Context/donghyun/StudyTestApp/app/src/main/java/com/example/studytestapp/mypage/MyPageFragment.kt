package com.example.studytestapp.mypage

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.studytestapp.BaseFragment
import com.example.studytestapp.R
import com.example.studytestapp.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth

class MyPageFragment  : BaseFragment<FragmentMypageBinding, MyPageViewModel>(), AuthListener {
    override fun getLayoutResourceId() = R.layout.fragment_mypage
    override val mViewModel: MyPageViewModel by viewModels()

    override fun initDataBinding() {
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initView() {
        mViewModel.init(this)
    }

    override fun authListener(auth: FirebaseAuth, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if(task.isSuccessful) {
                    mViewModel.successSignIn()
                } else {
                    Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun authSignUpListener(auth: FirebaseAuth, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if(task.isSuccessful) {
                    Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

interface AuthListener {
    fun authListener(auth: FirebaseAuth, email: String, password: String)
    fun authSignUpListener(auth: FirebaseAuth, email: String, password: String)
}