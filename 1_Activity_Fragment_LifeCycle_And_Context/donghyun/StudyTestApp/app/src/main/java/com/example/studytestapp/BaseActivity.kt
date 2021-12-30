package com.example.studytestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<DB: ViewDataBinding> : AppCompatActivity() {
    lateinit var mBinding: DB

    abstract fun getLayoutResourceId(): Int
    abstract fun initDataBinding()
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutResourceId())

        initDataBinding()
        initView()

    }
}