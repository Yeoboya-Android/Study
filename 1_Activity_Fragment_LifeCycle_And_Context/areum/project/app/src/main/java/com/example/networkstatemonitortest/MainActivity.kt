package com.example.networkstatemonitortest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.networkstatemonitortest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NetworkStateNavigator {

    lateinit var binding: ActivityMainBinding
    private val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewmodel = mViewModel
        }

        // 네트워크 상태 감지 라이프사이클 등록
        val networkStatusMonitor = NetworkStateMonitor(this)
        lifecycle.addObserver(networkStatusMonitor)
        networkStatusMonitor.setNavigator(this)
        networkStatusMonitor.initNetworkCheck()
    }

    override fun onConnect() {
        runOnUiThread { mViewModel.setNetworkStatus(true) }
    }

    override fun onDisConnect() {
        runOnUiThread { mViewModel.setNetworkStatus(false) }
    }
}