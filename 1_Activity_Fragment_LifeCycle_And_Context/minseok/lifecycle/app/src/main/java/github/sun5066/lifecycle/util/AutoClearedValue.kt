package github.sun5066.lifecycle.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {

    private var binding: T? = null

    init {
        var observerRegistered = false
        val viewObserver = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                observerRegistered = false
                binding = null
                super.onDestroy(owner)
            }
        }

        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                if (!observerRegistered) {
                    fragment.viewLifecycleOwner.lifecycle.addObserver(viewObserver)
                    observerRegistered = true
                }
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>) =
        binding ?: throw IllegalStateException("should never call auto-cleared-value get when it might not be available")

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        binding = value
    }
}

fun <T> Fragment.autoCleared() = AutoClearedValue<T>(this)