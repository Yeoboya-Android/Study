package com.example.lifecyclestudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lifecyclestudy.databinding.FragmentFirstBinding
import com.example.lifecyclestudy.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    lateinit var mBinding: FragmentSecondBinding
    private val mViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container,false)
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = mViewModel
        }

        return mBinding.root
    }
}