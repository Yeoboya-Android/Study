package com.lifecycletester.m.app.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.util.ActivityLifecycleObserver
import com.lifecycletester.m.app.util.ActivityUtil
import com.orhanobut.logger.Logger
import org.json.JSONObject

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment(), BaseFragmentNavigator
{
    /********************************************************/
    var mBinding: DB? = null
    abstract val mViewModel: VM

    var isInitView = false
    private val m_lifecycleObserver = ActivityLifecycleObserver(lifecycle)

    lateinit var m_frameLayout : FrameLayout
    lateinit var m_inflater: LayoutInflater
    /********************************************************/

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun initDataBinding()
    abstract fun initView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        m_inflater = inflater
        m_frameLayout = container!! as FrameLayout
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        initDataBinding()
        isInitView = false

        lifecycle.addObserver(m_lifecycleObserver)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mBinding = null
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Logger.i("onConfigurationChanged")

        m_frameLayout.removeAllViews()

        mBinding = DataBindingUtil.inflate(m_inflater, getLayoutResourceId(), m_frameLayout, false)
        initDataBinding()

        m_frameLayout.addView(mBinding!!.root)

        initView()

        when(newConfig.orientation)
        {
            Configuration.ORIENTATION_PORTRAIT->{
                //val aaa = mBinding!!.root as FrameLayout
                //aaa.removeAllViews()

                //val inflator = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)

                //mBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
            }
            Configuration.ORIENTATION_LANDSCAPE->{

            }
        }
    }
}