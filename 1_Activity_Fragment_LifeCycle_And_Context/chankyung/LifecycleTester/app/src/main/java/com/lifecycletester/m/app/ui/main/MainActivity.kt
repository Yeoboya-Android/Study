package com.lifecycletester.m.app.ui.main

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseActivity
import com.lifecycletester.m.app.databinding.ActivityMainBinding
import com.lifecycletester.m.app.ui.fragment.GpsFragment
import com.lifecycletester.m.app.util.ActivityLifecycleObserver
import pyxis.uzuki.live.richutilskt.utils.RPermission

class MainActivity : BaseActivity<ActivityMainBinding>(), MainNavigator {
    /****************************************************/
    private val mViewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private var mGpsFragment : GpsFragment? = null
    /****************************************************/

    @LayoutRes
    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun initDataBinding(){
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
    }

    override fun initView(){
        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        addGpsFragment()
    }

    fun addGpsFragment(){
        mGpsFragment = createFragment(GpsFragment::class.java, frameResId = R.id.content_gps)
    }

    override fun removeGpsFragment() {
        super.removeFragment(mGpsFragment)
    }

    override fun finishActivity() {
        super.finishActivity(false)
    }
}