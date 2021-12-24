package github.sun5066.lifecycle.ui.activity

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.R
import github.sun5066.lifecycle.databinding.ActivityMainBinding
import github.sun5066.lifecycle.ui.dialog.DetailDialog
import github.sun5066.lifecycle.ui.fragment.DetailFragment
import github.sun5066.lifecycle.ui.fragment.ListFragment
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.ui.state.LifeCycleModeState
import github.sun5066.lifecycle.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel by viewModels<MainViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            onResultPermission(granted)
        }

    private fun onResultPermission(granted: Boolean) {
        if (granted)
            showView()
        else
            finish()
    }

    override fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
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
                else -> throw NullPointerException("상태가 Null 입니다!")
            }
        )

    fun showDetailFragment(imageData: ImageData) {
        val bundle = bundleOf(Pair("imageData", imageData))

        findOrCreateFragment(DetailFragment::class.java, bundle, R.id.container)
    }

    // todo 여기 부분 수정해야됨
    private fun <T : Fragment> findOrCreateFragment(
        type: Class<T>,
        args: Bundle? = null,
        @IdRes id: Int
    ) = when (val currentFragment = supportFragmentManager.findFragmentById(id)) {
            type.javaClass -> currentFragment as T
            else -> {
                val instance: T = type.newInstance()
                args?.let { instance.arguments = it }

                supportFragmentManager.beginTransaction()
                    .addOrReplace(id, instance, currentFragment == null).apply {
                        if (mainViewModel.lifeCycleState.value is LifeCycleModeState.BackStack)
                         addToBackStack(null)
                    }.commitAllowingStateLoss()
                instance
            }
        }

    private fun showView(state: LastViewState? = mainViewModel.lastViewState.value) {
        when (state) {
            is LastViewState.DetailDialog -> {
                DetailDialog().apply {
                    arguments = bundleOf(Pair("imageData", state.imageData))
                    show(supportFragmentManager, "DetailDialog")
                }
            }
            is LastViewState.DetailFragment -> {
                findOrCreateFragment(
                    DetailFragment::class.java, bundleOf(Pair("imageData", state.imageData)), R.id.container
                )
            }
            is LastViewState.ListFragment -> {
                findOrCreateFragment(
                    ListFragment::class.java, null, R.id.container
                )
            }
            null -> throw IllegalStateException("null !!")
        }
    }
}

private fun FragmentTransaction.addOrReplace(@IdRes id: Int, fragment: Fragment, flag: Boolean) =
    if (flag)
        add(id, fragment, fragment::class.java.name)
    else
        replace(id, fragment, fragment::class.java.name)
