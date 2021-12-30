package com.example.lifecyclestudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.lifecyclestudy.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mViewModel : SharedViewModel by viewModels()
    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun initDataBinding() {
        mBinding.apply {
            lifecycleOwner = this@MainActivity
            viewmodel = mViewModel
        }
    }

    override fun initView() {
        supportFragmentManager.beginTransaction().add(R.id.frame1, FirstFragment()).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame2, SecondFragment()).commit()
    }

}