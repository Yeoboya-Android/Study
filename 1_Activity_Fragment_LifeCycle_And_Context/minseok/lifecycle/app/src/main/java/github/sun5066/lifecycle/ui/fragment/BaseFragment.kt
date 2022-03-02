package github.sun5066.lifecycle.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import github.sun5066.lifecycle.util.autoCleared

abstract class BaseFragment<B: ViewDataBinding> : Fragment() {

    private val TAG = this::class.java.simpleName

    protected var binding by autoCleared<B>()

    /**
     * 순서대로 실행
     * 바인딩 -> 리스트 어댑터 -> 뷰 -> 옵저버
     * */
    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?)
    abstract fun initViews(view: View)
    open fun fetchData() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        initViews(view)
        Log.d("123", "$TAG 333::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onResume() {
        super.onResume()
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("123", "$TAG::${Thread.currentThread().stackTrace[4].methodName}")
    }
}