package com.lifecycletester.m.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
//import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.lifecycletester.m.app.R
import com.orhanobut.logger.Logger

const val GPS_LAST_LAT_KEY : String = "lastLat"
const val GPS_LAST_LNG_KEY : String = "lastLng"

const val GPS_SERVICE_CHECK_SUCCESS : Int = 0       // GPS 서비스 활성화되어 있음
const val GPS_SERVICE_CHECK_SETTING : Int = 1       // GPS 서비스 켜라
const val GPS_SERVICE_CHECK_FAIL : Int = 2          // GPS 서비스 키지 않는다.

/**********************************************************************************************
 * @FileName  : GpsHelper.kt
 * @Date       : 2019.08.07
 * @작성자     : Diamondstep
 * @설명       : Gps 위치좌표 얻기.
 **********************************************************************************************/
class GpsHelper(val context : Context, val lifecycle: Lifecycle) : LifecycleObserver {
    /**********************************************************************************************/
    lateinit var m_googleApiClient : GoogleApiClient
    val m_gpsChecker = GpsListener()

    var m_callbackFunc : ((Double, Double, Boolean)->Unit)? = null
    var m_isCheckingGps : Boolean = false       // 위치설정 액티비티 창 실행했나?
    var m_isRefreshGps : Boolean = false        // 위치정보 새로 얻어 올것인가? 아니면 마지막 위치정보 얻어 올것인가?
    /**********************************************************************************************/

    public fun init(){
        m_googleApiClient = GoogleApiClient.Builder(context)
                                           .addApi(LocationServices.API)
                                           .addConnectionCallbacks(m_gpsChecker)
                                           .addOnConnectionFailedListener(m_gpsChecker)
                                           .build()

        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public fun onResume(){
        if(false == m_isCheckingGps) return

        m_isCheckingGps = false
        doGps(false)
    }

    public fun getGpsPos(_isRefresh : Boolean, _isAlert : Boolean, _callback:((Double?, Double?, Boolean)->Unit)){
        try {
            require(false == m_googleApiClient.isConnected)

            m_isRefreshGps = _isRefresh
            m_callbackFunc = _callback

            doGps(_isAlert)
        }catch (e : java.lang.Exception){
            Logger.i("getGpsPosError : ${e.message.toString()}")
        }
    }

    private fun doGps(_isAlert : Boolean){
        checkGpsService(_isAlert) {
            when(it)
            {
                GPS_SERVICE_CHECK_SETTING ->{
                    m_isCheckingGps = true

                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    context.startActivity(intent)
                }

                GPS_SERVICE_CHECK_FAIL ->{
                    onComplete(0.0, 0.0, false)
                }

                else ->{
                    m_googleApiClient.connect()
                }
            }
        }
    }

    private fun checkGpsService(_isAlert : Boolean, _result : (Int)->Unit){
        val manager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(true == manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            _result(GPS_SERVICE_CHECK_SUCCESS)
            return
        }

        if(false == _isAlert){
            _result(GPS_SERVICE_CHECK_FAIL)
            return
        }

        AlertDialog.Builder(context).apply {
                                                setIcon(R.drawable.ic_launcher)
                                                setTitle("위치서비스 설정")
                                                setMessage("GPS를 켜시면 나와 가까운 상대를 검색할 수 있습니다.\n서비스를 설정하시겠습니까?\n")
                                                setPositiveButton("설정") { _, _ ->
                                                    _result(GPS_SERVICE_CHECK_SETTING)
                                                }
                                                setNegativeButton("취소"){_, _ ->
                                                    _result(GPS_SERVICE_CHECK_FAIL)
                                                }
                                                create()
                                            }.show()
    }

    private fun onComplete(_location : Location, _isSuccess : Boolean){
        m_googleApiClient.disconnect()

        if(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)){
            m_callbackFunc?.invoke(_location.latitude, _location.longitude, _isSuccess)
        }
    }

    private fun onComplete(_latitude : Double, _longitude : Double, _isSuccess : Boolean){
        m_googleApiClient.disconnect()

        if(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)){
            m_callbackFunc?.invoke(_latitude, _longitude, _isSuccess)
        }
    }

    @SuppressLint("MissingPermission")
    inner class GpsListener : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    {
        override fun onConnected(p0: Bundle?){
            try {
                if(m_isRefreshGps){
                    doRefreshGps()
                }
                else{
                    LocationServices.FusedLocationApi.getLastLocation(m_googleApiClient)?.let{
                        onComplete(it, true)
                    } ?: doRefreshGps()
                }
            }
            catch (_e : Exception){
                //FirebaseCrashlytics.getInstance().recordException(_e)
                //Crashlytics.log("getGeoPosition Erro 오류 : $_e")

                onComplete(0.0, 0.0, false)
            }
        }

        override fun onConnectionFailed(p0: ConnectionResult){
            //Toast.makeText(_context, "onConnectionFailed()", Toast.LENGTH_LONG).show()
            /*m_googleApiClient.disconnect()
            m_callbackFunc?.invoke(0.0, 0.0)*/
        }

        override fun onConnectionSuspended(p0: Int){
            //Toast.makeText(_context, "onConnectionSuspended()", Toast.LENGTH_LONG).show()
            /*m_googleApiClient.disconnect()
            m_callbackFunc?.invoke(0.0, 0.0)*/
        }

        private fun doRefreshGps(){
            //Toast.makeText(context, "위치정보 못받음", Toast.LENGTH_LONG).show()
            try {
                val locationRequest = LocationRequest()
                with(locationRequest){
                    interval = 100
                    fastestInterval = 100
                    setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                }

                LocationServices.FusedLocationApi.requestLocationUpdates(m_googleApiClient, locationRequest){
                    if(m_googleApiClient.isConnected){
                        LocationServices.FusedLocationApi.removeLocationUpdates(m_googleApiClient){}
                        onComplete(it, true)
                    }
                }
            }catch (e : Exception){
                e.message
            }
        }
    }
}