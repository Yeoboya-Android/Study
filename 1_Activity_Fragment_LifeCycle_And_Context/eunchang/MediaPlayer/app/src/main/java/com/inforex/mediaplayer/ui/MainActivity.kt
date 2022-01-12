package com.inforex.mediaplayer.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.databinding.ActivityMainBinding
import com.inforex.mediaplayer.ui.base.BaseActivity
import com.inforex.mediaplayer.ui.base.BaseNavigator


/** todo
 * Api로 playList 받아오기
 * Service 작업
 * Coroutine 작업
 * */

class MainActivity : BaseActivity<ActivityMainBinding>(), BaseNavigator {

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

    override fun gotoLocalMediaListFragment(showFragment: Boolean) {
        if (showFragment)
            findOrCreateFragment(LocalMediaFragment::class.java, Bundle(), R.id.container_contents_fragment)
        else
            removeFragmentById(intArrayOf(R.id.container_contents_fragment))
    }

    override fun gotoTapeMediaListFragment(showFragment: Boolean) {

    }

    override fun showFloatingFragment(showFragment: Boolean) {
        if (showFragment)
            findOrCreateFragment(FloatingMediaFragment::class.java, Bundle(), R.id.container_floating_fragment)
        else
            removeFragmentById(intArrayOf(R.id.container_floating_fragment))

    }

}