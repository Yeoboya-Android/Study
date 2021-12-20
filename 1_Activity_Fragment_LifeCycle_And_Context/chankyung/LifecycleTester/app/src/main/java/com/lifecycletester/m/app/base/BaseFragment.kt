package com.lifecycletester.m.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.util.ActivityLifecycleObserver
import com.lifecycletester.m.app.util.ActivityUtil
import org.json.JSONObject

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment(), BaseFragmentNavigator
{
    /********************************************************/
    lateinit var mBinding: DB
    abstract val mViewModel: VM

    var isInitView = false
    private val m_lifecycleObserver = ActivityLifecycleObserver(lifecycle)
    /********************************************************/

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun initDataBinding()
    abstract fun initView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        initDataBinding()
        isInitView = false

        lifecycle.addObserver(m_lifecycleObserver)

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()

        if(!isInitView){
            initView()
            isInitView = true
        }
    }

    fun <T : Fragment> createFragment(type: Class<T>, _param: JSONObject? = null, frameResId: Int = R.id.content): T {
        val fragmentManager = childFragmentManager

        val instance: T = type.newInstance()
        _param?.let {
            instance.arguments = Bundle(1).apply { putString("data", _param.toString()) }
        }

        ActivityUtil.addFragmentToActivity(fragmentManager, instance, frameResId)

        return instance
    }

    protected fun removeFragment(fragment: Fragment?) {
        fragment?.let {
            ActivityUtil.delFragmentToActivity(childFragmentManager, it)
        }
    }
}