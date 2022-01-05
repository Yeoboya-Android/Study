package com.inforex.mediaplayer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    var mBinding: DB? = null
    abstract val mViewModel: VM

    var isInitView = false
        private set

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun initDataBinding()
    abstract fun initView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        initDataBinding()

        return mBinding!!.root
    }

    override fun onResume() {
        super.onResume()

        initView()
        isInitView = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}