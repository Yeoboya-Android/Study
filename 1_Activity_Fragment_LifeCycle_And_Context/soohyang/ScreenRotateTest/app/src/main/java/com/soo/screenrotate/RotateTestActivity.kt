package com.soo.screenrotate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.soo.screenrotate.databinding.ActivityRotateTestBinding
import com.soo.screenrotate.fragments.BlueFragment
import com.soo.screenrotate.fragments.RedFragment
import com.soo.screenrotate.utils.ActivityUtil
import com.soo.screenrotate.viewmodels.RotateTestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RotateTestActivity : AppCompatActivity() {

    private val mViewModel: RotateTestViewModel by viewModels()
    lateinit var mBinding: ActivityRotateTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityRotateTestBinding>(this, R.layout.activity_rotate_test)
        mBinding.apply {
            lifecycleOwner = this@RotateTestActivity
            viewModel = mViewModel
            clickListener = onClickListener
        }
    }


    private val onClickListener = View.OnClickListener {
        when(it.id) {
            R.id.btn_red -> {
                findOrCreateFragment(RedFragment::class.java, null, R.id.content, true)
            }
            R.id.btn_blue -> {
                findOrCreateFragment(BlueFragment::class.java, null, R.id.content, true)
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : Fragment> findOrCreateFragment(type: Class<T>, args: Bundle? = null, frameResId: Int = R.id.content, addBackStack: Boolean = true): T {
        val currentFragment = supportFragmentManager.findFragmentById(frameResId)

        return when {
            isFinishing -> currentFragment as T
            currentFragment == null -> {
                val instance: T = type.newInstance()
                args?.let{ instance.arguments = it }

                ActivityUtil.addFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)
                instance
            }
            currentFragment.javaClass == type -> currentFragment as T

            else -> {
                val instance: T = type.newInstance()
                args?.let{ instance.arguments = it }

                ActivityUtil.replaceFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)
                instance
            }
        }
    }

}