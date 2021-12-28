package com.example.studytestapp.home

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.studytestapp.BaseFragment
import com.example.studytestapp.R
import com.example.studytestapp.databinding.FragmentHomeBinding
import com.google.firebase.messaging.FirebaseMessaging

class HomeFragment  : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getLayoutResourceId() = R.layout.fragment_home
    override val mViewModel: HomeViewModel by viewModels()

    override fun initDataBinding() {
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initView() {
        mViewModel.init()
        initFirebase()

    }

    // firebase 토큰 가져오기
    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   Log.d("동현","${task.result}")
                } else { }
            }
    }
}