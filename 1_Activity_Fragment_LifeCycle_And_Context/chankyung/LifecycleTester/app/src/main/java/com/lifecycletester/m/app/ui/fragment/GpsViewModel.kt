package com.lifecycletester.m.app.ui.fragment

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseViewModel
import com.lifecycletester.m.app.util.GpsHelper
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class GpsViewModel : BaseViewModel()
{
    /*********************************************************/
    lateinit var m_context : Context
    lateinit var m_navigator : MainNavigator

    lateinit var m_gpsHelper : GpsHelper

    private var obsGpsValue = MutableLiveData<String>("")
    val gpsValue : LiveData<String> get() = obsGpsValue
    /*********************************************************/

    val onClickListener = View.OnClickListener {
        when(it.id)
        {
            R.id.btn_check_gps->{
                super.addDisposable(
                    Observable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe {
                        Logger.i("aaaaaaaa")
                        m_gpsHelper.getGpsPos(true, true){_lat, _lng, _isSuccess->
                            Logger.i("${_lat}, ${_lng}, ${_isSuccess}")
                            obsGpsValue.value = String.format("${_lat}, ${_lng}, ${_isSuccess}")

                            Toast.makeText(m_context, "aaaaaa", Toast.LENGTH_SHORT).show()
                        }
                    })

                m_navigator.removeGpsFragment()
            }
        }
    }

    fun init(_context : Context, _navigator : MainNavigator, _lifeCycle : Lifecycle){
        m_context = _context
        m_navigator = _navigator

        m_gpsHelper = GpsHelper(_context, _lifeCycle).apply {
            init()
        }
        //m_context = _context
    }
}