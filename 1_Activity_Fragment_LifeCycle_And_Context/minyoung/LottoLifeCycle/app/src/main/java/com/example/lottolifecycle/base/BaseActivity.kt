package com.example.lottolifecycle.base

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lottolifecycle.BR
import com.example.lottolifecycle.viewmodel.BaseViewModel
import java.lang.Exception

abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel>(@LayoutRes private val layoutResId : Int) : AppCompatActivity() {

    lateinit var binding : T
    protected lateinit var viewmodel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.setLifecycleOwner { this.lifecycle }
        initDataBinding()
        initViewModel()
        initActivity()

    }

    abstract fun initDataBinding()
    abstract fun initActivity()

    private fun initViewModel(){
        try {
            binding.setVariable(BR.viewmodel, viewmodel)
        }catch (e:Exception){
            Log.e("BaseActivity 에러", e.toString())
        }
    }

    fun addFragment(containerViewId : Int, mFragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(containerViewId, mFragment)
        transaction.commitAllowingStateLoss()
    }

    fun removeFragment(mFragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(mFragment)
        transaction.commitAllowingStateLoss()
    }


}