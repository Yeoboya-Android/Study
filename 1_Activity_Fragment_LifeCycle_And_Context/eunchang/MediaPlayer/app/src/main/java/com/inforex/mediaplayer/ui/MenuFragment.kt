package com.inforex.mediaplayer.ui

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.databinding.FragmentFloatingMediaBinding
import com.inforex.mediaplayer.databinding.FragmentMenuBinding
import com.inforex.mediaplayer.ui.base.BaseFragment

class MenuFragment : BaseFragment<FragmentMenuBinding, MainViewModel>() {

    override val mViewModel: MainViewModel by activityViewModels()

    override fun getLayoutResourceId() = R.layout.fragment_menu

    override fun initDataBinding() {
        mBinding?.vm = mViewModel
        mBinding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.i("qwe123", "MenuFragment.handleOnBackPressed():::")
        }
    }

}
