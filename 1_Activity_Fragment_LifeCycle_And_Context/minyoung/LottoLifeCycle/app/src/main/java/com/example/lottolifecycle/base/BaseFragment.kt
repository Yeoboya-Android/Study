package com.example.lottolifecycle.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lottolifecycle.BR
import com.example.lottolifecycle.viewmodel.BaseViewModel
import java.lang.Exception

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel>(@LayoutRes private val layoutResId : Int) : Fragment() {

    lateinit var binding : T
    protected lateinit var viewmodel : VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        val view : View = binding.root
        binding.lifecycleOwner = viewLifecycleOwner
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatabinding()
        initViewModel()
        initFragment()
    }

    abstract fun initDatabinding()
    abstract fun initFragment()

    private fun initViewModel(){
        try{
            binding.setVariable(BR.viewmodel, viewmodel)
        }catch (e:Exception){
            Log.e("BaseFragment 에러", e.toString())
        }

    }

    fun addFragment(containerViewId : Int, mFragment : Fragment){
        activity?.let {
            val transaction = it.supportFragmentManager.beginTransaction()
            transaction.replace(containerViewId, mFragment)
            transaction.commitAllowingStateLoss()
        }
    }

    fun removeFragment(mFragment: Fragment){
        activity?.let {
            val transaction = it.supportFragmentManager.beginTransaction()
            transaction.remove(mFragment)
            transaction.commitAllowingStateLoss()
        }
    }


}