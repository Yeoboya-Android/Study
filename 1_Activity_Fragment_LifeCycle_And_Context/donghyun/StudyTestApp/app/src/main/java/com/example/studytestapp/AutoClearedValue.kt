package com.example.studytestapp

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {

    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object: DefaultLifecycleObserver {
             // 구조를 보면 viewLifecycleOwnerLiveData 구독
            val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> { lifecycleOwner ->
                val viewLifecycleOwner = lifecycleOwner ?: return@Observer
                viewLifecycleOwner.lifecycle.addObserver(object: DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        Log.d("동현","addObserver - onDestroy")
                        binding = null
                    }
                })
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)  //참고) observeForever(Observer) 메소드를 사용하면 연결된 LifecycleOwner 없이 Observer를 등록할 수 있다. 이 경우 Observer는 항상 활성 상태로 간주되므로 항상 수정 관련 알림을 받는다.
            }

            override fun onDestroy(owner: LifecycleOwner) {
                Log.d("동현","removeObserver")
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
            }
        })
    }


    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding ?: throw IllegalStateException("should never call auto-cleared-value get when it might not be available")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        binding = value
    }
}

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)