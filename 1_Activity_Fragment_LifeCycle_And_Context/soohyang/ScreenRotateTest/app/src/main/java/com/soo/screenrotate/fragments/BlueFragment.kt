package com.soo.screenrotate.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.soo.screenrotate.LiveActivity
import com.soo.screenrotate.R
import com.soo.screenrotate.databinding.FragmentBlueBinding
import com.soo.screenrotate.viewmodels.BlueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlueFragment : Fragment() {

    private val mViewModel: BlueViewModel by activityViewModels()
    lateinit var binding: FragmentBlueBinding

    companion object {
        val SAVE_KEY_FRAGMENT_COUNT = "save_key_fragment_count"
    }

    private var fragmentCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d("soohyangA", "BlueFragment onCreateView")

        binding = DataBindingUtil.inflate<FragmentBlueBinding>(
                inflater,
                R.layout.fragment_blue,
                container,
                false
        ).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            clickListener = onClickListener
        }

        if(savedInstanceState != null) {
            fragmentCount = savedInstanceState.getInt(SAVE_KEY_FRAGMENT_COUNT, 0)
        }

        binding.textFragmentCount.text = fragmentCount.toString()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVE_KEY_FRAGMENT_COUNT, fragmentCount)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_view_model_count -> {
                mViewModel.addCount()
            }
            R.id.btn_fragment_count -> {
                fragmentCount = fragmentCount.plus(1)
                binding.textFragmentCount.text = fragmentCount.toString()
            }
        }
    }
}