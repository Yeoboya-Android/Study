package com.inforex.mediaplayer.ui

import androidx.fragment.app.activityViewModels
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.databinding.FragmentFloatingMediaBinding
import com.inforex.mediaplayer.ui.base.BaseFragment

class FloatingMediaFragment : BaseFragment<FragmentFloatingMediaBinding, MainViewModel>() {

    override val mViewModel: MainViewModel by activityViewModels()

    override fun getLayoutResourceId() = R.layout.fragment_floating_media

    override fun initDataBinding() {
        mBinding?.vm = mViewModel
        mBinding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onPause() {
        super.onPause()
    }

    override fun initView() {

    }

}
