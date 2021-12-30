package com.lifecycletester.m.app.ui.main

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseActivity
import com.lifecycletester.m.app.databinding.ActivityMainBinding
import com.lifecycletester.m.app.ui.fragment.ChatFragment
import com.lifecycletester.m.app.ui.fragment.GpsFragment
import com.lifecycletester.m.app.ui.sub.SubActivity
import com.lifecycletester.m.app.util.ActivityLifecycleObserver
import pyxis.uzuki.live.richutilskt.utils.RPermission
import java.io.Serializable

class MainActivity : BaseActivity<ActivityMainBinding>(), MainNavigator {
    /****************************************************/
    private val mViewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    /****************************************************/

    @LayoutRes
    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun initDataBinding(){
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
    }

    override fun initView(){
        RPermission.instance.checkPermission(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))

        mViewModel.init(this)
    }

    override fun createGpsFragment() {
        createFragment(GpsFragment::class.java, frameResId = R.id.content_gps)
    }

    override fun replaceGpsFragment() {
        val fragment = findOrCreateFragment(GpsFragment::class.java, frameResId = R.id.content_gps)
    }

    override fun createChatFragment() {
        createFragment(ChatFragment::class.java, null, frameResId = R.id.content_gps)
    }

    override fun replaceChatFragment() {
        val fragment = findOrCreateFragment(ChatFragment::class.java, null, frameResId = R.id.content_gps)
    }

    override fun createSubActivity() {
        val intent = Intent(this, SubActivity::class.java)
        startActivity(intent)
    }

    override fun removeFragment() {
        popFragment(R.id.content_gps)
    }

    override fun finishActivity() {
        super.finishActivity(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("frag", "MainActivity::onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("frag", "MainActivity::onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("frag", "MainActivity::onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("frag", "MainActivity::onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("frag", "MainActivity::onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("frag", "MainActivity::onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("frag", "MainActivity::onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("frag", "MainActivity::onRestoreInstanceState")
    }
}