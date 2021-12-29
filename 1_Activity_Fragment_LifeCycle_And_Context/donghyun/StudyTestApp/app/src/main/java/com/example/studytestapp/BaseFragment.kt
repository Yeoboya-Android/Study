package com.example.studytestapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleObserver

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    // 바인딩의 초기화를 autoCleared로 위임
    var mBinding by autoCleared<DB>()
    abstract val mViewModel: VM

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun initDataBinding()
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        initDataBinding()
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
    }
}