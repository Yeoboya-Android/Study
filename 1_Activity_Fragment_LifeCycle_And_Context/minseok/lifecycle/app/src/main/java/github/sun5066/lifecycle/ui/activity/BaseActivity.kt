package github.sun5066.lifecycle.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B: ViewBinding> : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    protected lateinit var binding: B

    abstract fun initBinding()
    open fun initPermission() {}
    open fun initView() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setContentView(binding.root)
        initPermission()
        initView()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onResume() {
        super.onResume()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }
}