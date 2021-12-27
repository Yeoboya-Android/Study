package com.example.networkstatemonitortest

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NetworkStateMonitor(private val lifecycleOwner: AppCompatActivity): ConnectivityManager.NetworkCallback(), LifecycleObserver
{
    companion object {
        const val TAG = "NetworkStateMonitor"
    }

    private var networkStateNavigator: NetworkStateNavigator? = null

    fun setNavigator(networkStateNavigator: NetworkStateNavigator?) {
        this.networkStateNavigator = networkStateNavigator
    }

    /** 앱의 시스템의 연결 상태를 알리는 ConnectivityManager 등록 **/
    private val connectivityManager = lifecycleOwner.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /** NetworkRequest를 빌드하여 수신 대기하려는 네트워크의 종류를 ConnectivityManager에 알림 **/
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    /** 네트워크 상태 체크 **/
    fun initNetworkCheck() {
        val activeNetwork = connectivityManager.activeNetwork
        if(activeNetwork != null) {
            Log.e(TAG,"네트워크 초기 상태 : 연결되어 있음")
        } else {
            Log.e(TAG,"네트워크 초기 상태 : 연결되어 있지않음")
        }
    }

    /** 네트워크 모니터링 시작 **/
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerNetworkStateMonitor() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
        Log.e(TAG,"registerNetworkStateMonitor() ")
    }

    /** 네트워크 모니터링 해제 **/
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterNetworkStateMonitor() {
        connectivityManager.unregisterNetworkCallback(this)
        lifecycleOwner.lifecycle.removeObserver(this)
        networkStateNavigator = null
        Log.e(TAG,"unregisterNetworkStateMonitor()")
    }

    /** 네트워크 상태 : onAvailable **/
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateNavigator?.onConnect()
        Log.e(TAG,"networkAvailable()")
    }

    /** 네트워크 상태 : onUnavailable**/
    override fun onLost(network: Network) {
        super.onLost(network)
        networkStateNavigator?.onDisConnect()
        Log.e(TAG,"networkUnavailable()")
    }
}

