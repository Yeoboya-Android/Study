package com.soo.screenrotate.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.soo.screenrotate.R
import com.soo.screenrotate.databinding.FragmentRedBinding
import com.soo.screenrotate.viewmodels.RedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedFragment : Fragment() {

    private val mViewModel: RedViewModel by activityViewModels()
    lateinit var binding: FragmentRedBinding

    private var fragmentCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d("soohyangA", "RedFragment onCreateView")

        binding = DataBindingUtil.inflate<FragmentRedBinding>(
                inflater,
                R.layout.fragment_red,
                container,
                false
        ).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            clickListener = onClickListener
        }

        binding.textFragmentCount.text = fragmentCount.toString()

        return binding.root
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