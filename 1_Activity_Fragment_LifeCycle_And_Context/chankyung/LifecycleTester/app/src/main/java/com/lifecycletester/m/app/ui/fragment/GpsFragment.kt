package com.lifecycletester.m.app.ui.fragment

import android.Manifest
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseFragment
import com.lifecycletester.m.app.databinding.FragmentGpsBinding
import com.lifecycletester.m.app.util.OnBackPressedListener
import com.orhanobut.logger.Logger
import pyxis.uzuki.live.richutilskt.utils.RPermission

class GpsFragment : BaseFragment<FragmentGpsBinding, GpsViewModel>(), OnBackPressedListener
{
    /*********************************************************/
    override val mViewModel: GpsViewModel by lazy { ViewModelProvider(this).get(GpsViewModel::class.java) }

    private lateinit var m_navigator: MainNavigator
    /*********************************************************/

    @LayoutRes
    override fun getLayoutResourceId() = R.layout.fragment_gps

    override fun initDataBinding(){
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
    }

    override fun initView(){
        m_navigator = activity as MainNavigator
        m_navigator.setOnBackButtonTouchListener(this)

        mViewModel.init(requireContext(), m_navigator, lifecycle)
    }

    override var onSuperBackPressed: Boolean = false
    override fun onBackPressed() {
        m_navigator.finishActivity()
    }
}