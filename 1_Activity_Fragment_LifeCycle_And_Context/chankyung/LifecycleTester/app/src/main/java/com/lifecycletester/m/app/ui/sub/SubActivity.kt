package com.lifecycletester.m.app.ui.sub

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseActivity
import com.lifecycletester.m.app.databinding.ActivitySubBinding
import com.lifecycletester.m.app.ui.fragment.GpsFragment

class SubActivity : BaseActivity<ActivitySubBinding>()
{
    /****************************************************/
    private val mViewModel: SubViewModel by lazy { ViewModelProvider(this).get(SubViewModel::class.java) }
    private var m_listGpsFragment = mutableListOf<GpsFragment>()
    /****************************************************/

    @LayoutRes
    override fun getLayoutResourceId(): Int = R.layout.activity_sub

    override fun initDataBinding(){
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
    }

    override fun initView(){
        addGpsFragment()
    }

    fun addGpsFragment(){
        m_listGpsFragment.add(createFragment(GpsFragment::class.java, frameResId = R.id.content_gps))
    }
}