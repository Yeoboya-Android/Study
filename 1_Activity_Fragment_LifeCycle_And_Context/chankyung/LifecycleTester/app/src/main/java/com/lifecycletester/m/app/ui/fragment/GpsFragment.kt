package com.lifecycletester.m.app.ui.fragment

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseFragment
import com.lifecycletester.m.app.base.BaseNavigator
import com.lifecycletester.m.app.databinding.FragmentGpsBinding
import com.lifecycletester.m.app.util.OnBackPressedListener
import com.orhanobut.logger.Logger
import pyxis.uzuki.live.richutilskt.utils.RPermission

class GpsFragment : BaseFragment<FragmentGpsBinding, GpsViewModel>(), OnBackPressedListener
{
    /*********************************************************/
    override val mViewModel: GpsViewModel by lazy { ViewModelProvider(this).get(GpsViewModel::class.java) }

    private lateinit var m_navigator: BaseNavigator
    /*********************************************************/

    @LayoutRes
    override fun getLayoutResourceId() = R.layout.fragment_gps

    override fun initDataBinding(){
        mBinding!!.viewModel = mViewModel
        mBinding!!.lifecycleOwner = viewLifecycleOwner
    }

    override fun initView(){
        m_navigator = activity as BaseNavigator
        m_navigator.setOnBackButtonTouchListener(this)

        mViewModel.init(m_navigator, lifecycle)
    }

    override var onSuperBackPressed: Boolean = false
    override fun onBackPressed() {
        m_navigator.finishActivity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("frag", "GpsFragment::onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("frag", "GpsFragment::onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

        Log.d("frag", "GpsFragment::onCreateView")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("frag", "GpsFragment::onViewCreated")
    }

    override fun onStart() {
        super.onStart()

        Log.d("frag", "GpsFragment::onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("frag", "GpsFragment::onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("frag", "GpsFragment::onPause")
    }

    override fun onStop() {
        super.onStop()

        Log.d("frag", "GpsFragment::onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("frag", "GpsFragment::onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("frag", "GpsFragment::onDestroy")
    }

    override fun onDetach() {
        super.onDetach()

        Log.d("frag", "GpsFragment::onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.d("frag", "GpsFragment::onSaveInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Log.d("frag", "GpsFragment::onViewStateRestored")
    }
}