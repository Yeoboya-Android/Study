package com.inforex.mediaplayer.ui

import android.Manifest
import android.os.Bundle
import android.os.Process
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.databinding.ActivityMainBinding
import com.inforex.mediaplayer.ui.base.BaseActivity
import com.inforex.mediaplayer.ui.base.BaseNavigator


/** todo
 * Service 작업
 * */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mViewModel: MainViewModel by viewModels()

    @LayoutRes
    override fun getLayoutResourceId() = R.layout.activity_main

    override fun initDataBinding() {
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = this
    }

    override fun initView() {
        mViewModel.init(applicationContext, this)
        findOrCreateFragment(MenuFragment::class.java, Bundle(), R.id.container_menu_fragment)
        requestPermission()
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        enableButtons(isGranted)
    }

    private fun enableButtons(isEnable: Boolean) {
        mViewModel.setButtonEnable(isEnable)
    }


    override fun closeApp() {
        mViewModel.stopMediaPlayer() // todo MediaPlayerManager 구현하고 BaseActivity로 빼기
        moveTaskToBack(true)
        finish()
        Process.killProcess(Process.myPid())
    }
}