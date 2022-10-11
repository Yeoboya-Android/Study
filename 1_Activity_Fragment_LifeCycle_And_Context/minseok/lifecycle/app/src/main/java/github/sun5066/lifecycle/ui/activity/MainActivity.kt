package github.sun5066.lifecycle.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.TestService
import github.sun5066.lifecycle.databinding.ActivityMainBinding
import github.sun5066.lifecycle.ui.fragment.DetailFragment
import github.sun5066.lifecycle.ui.fragment.ListFragment
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.ui.state.LifeCycleModeState
import github.sun5066.lifecycle.viewmodel.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var testServiceBinder: TestService.LocalBinder? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            testServiceBinder = service as TestService.LocalBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            testServiceBinder = null
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            onResultPermission(granted)
        }

    private fun onResultPermission(granted: Boolean) {
        if (granted)
            mainViewModel.setLifeCycleModeState(LifeCycleModeState.Default)
        else
            finish()
    }

    override fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        mainViewModel.lifeCycleState.observe(this) { state ->
            if (state is LifeCycleModeState.Default || state is LifeCycleModeState.BackStack) {
                showListFragment()
            }
        }

        binding.btn.setOnClickListener {
            testServiceBinder?.service?.count()
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, TestService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(serviceConnection)
    }

    private fun showListFragment() {
        val isBackStack = mainViewModel.lifeCycleState.value is LifeCycleModeState.BackStack

        when (mainViewModel.lastViewState.value!!) {
            LastViewState.ListFragment,
            LastViewState.UnInitialize -> {
                findOrCreateFragment(ListFragment::class.java, null, R.id.container, isBackStack)
            }
            else -> Unit
        }
    }

    override fun initPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun onClickListeners(view: View) =
        mainViewModel.setLifeCycleModeState(
            when (view.id) {
                R.id.radio1 -> LifeCycleModeState.Default
                R.id.radio2 -> LifeCycleModeState.BackStack
                R.id.radio3 -> LifeCycleModeState.Dialog
                R.id.radio4 -> LifeCycleModeState.NewActivity
                else -> throw IllegalStateException("상태가 없습니다.")
            }
        )

    fun showDetailFragment(imageData: ImageData) = supportFragmentManager.apply {
        val args = bundleOf(Pair("imageData", imageData))
        val isBackStack = mainViewModel.lifeCycleState.value is LifeCycleModeState.BackStack
        findOrCreateFragment(DetailFragment::class.java, args, R.id.container, isBackStack)
    }

    private fun <T : Fragment> findOrCreateFragment(type: Class<T>, args: Bundle? = null, frameResId: Int = R.id.content, addBackStack: Boolean = true): T? {
        val currentFragment = supportFragmentManager.findFragmentById(frameResId)

        return when {
            isFinishing -> null
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

object ActivityUtil {
    // Activity FrameLayout에 Fragment add.
    fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
        fragmentManager.beginTransaction().add(frameId, fragment, fragment::class.java.name).apply {
            if (addBackStack) addToBackStack(null)
        }.commitAllowingStateLoss()

    // 현재 fragment replace
    fun replaceFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
        fragmentManager.beginTransaction().replace(frameId, fragment, fragment::class.java.name).apply {
            if (addBackStack) addToBackStack(null)
        }.commitAllowingStateLoss()
}