package com.inforex.mediaplayer.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.util.ActivityUtil


abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mBinding: DB

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun initDataBinding()

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, getLayoutResourceId())

        initDataBinding()
        initView()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T : Fragment> findOrCreateFragment(type: Class<T>, args: Bundle? = null, frameResId: Int, addBackStack: Boolean = true): T? {
        val currentFragment = supportFragmentManager.findFragmentById(frameResId)

        return when {
            isFinishing -> null
            currentFragment == type -> currentFragment as T
            currentFragment == null -> {
                val instance: T = type.newInstance()
                args?.let{ instance.arguments = it }

                ActivityUtil.addFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)
                instance
            }
            else -> {
                val instance: T = type.newInstance()
                args?.let{ instance.arguments = it }

                ActivityUtil.replaceFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)
                instance
            }
        }
    }

    protected fun <T : Fragment> createFragment(type: Class<T>, args: Bundle? = null, frameResId: Int, addBackStack: Boolean = true): T {
        val instance: T = type.newInstance()
        args?.let{ instance.arguments = it }

        ActivityUtil.addFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)

        return instance
    }

    protected fun getFragmentByTag(tag: String) = supportFragmentManager.findFragmentByTag(tag)

    protected fun removeFragment(fragment: Fragment?) {
        fragment?.let {
            ActivityUtil.removeFragmentToActivity(supportFragmentManager, it)
        }
    }

    protected fun removeFragmentById(removeId: IntArray) {
        supportFragmentManager.fragments.forEach {
            if (removeId.contains(it.id)) removeFragment(it)
        }
    }

    protected fun setFragmentResult(requestKey: String, cmd: String, result: Bundle) {
        val fragment = getFragmentByTag(requestKey)
        fragment?.run {
            supportFragmentManager.setFragmentResult(requestKey, result.apply { putString("cmd", cmd) })
        }
    }


}