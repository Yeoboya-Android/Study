package com.app.lifecycletest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.app.lifecycletest.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    val TAG = "햄" + FirstFragment::class.java.simpleName
    lateinit var mBinding: FragmentFirstBinding

    // fragment 간의 데이터 공유가 필요할때는 activity에서는 ViewModel을 초기화할 필요없이 fragment에서만 activityViewModels을 활용한다.
    // activity의 ViewModelStoreOwner를 활용하기 때문에 Fragment만 초기화가 존재하더라도 Activity가 관리하는 ViewModel이 생성된다.
    private val viewModelActivity: MainViewModel by activityViewModels()
    private val viewmodel: MainViewModel by viewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e(TAG, "onCreateView")

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container,false)
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            activityViewModel = viewModelActivity
            viewModel = fragmentViewModel
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")

        mBinding.button.setOnClickListener {
            fragmentViewModel.add()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "onDetach")
    }
}