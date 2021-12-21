package github.sun5066.lifecycle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import github.sun5066.lifecycle.util.autoCleared

abstract class BaseFragment<B: ViewBinding> : Fragment() {

    protected val binding by autoCleared<B>()

    open fun initViews(view: View) {}
    open fun fetchObservables() {}

    init {
        viewLifecycleOwnerLiveData.observe(viewLifecycleOwner, {
            fetchObservables()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }
}