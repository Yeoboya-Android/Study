package com.example.lottolifecycle.ui

import androidx.fragment.app.activityViewModels
import com.example.lottolifecycle.R
import com.example.lottolifecycle.base.BaseFragment
import com.example.lottolifecycle.databinding.FragmentLottoBinding
import com.example.lottolifecycle.utils.LottoUtils
import com.example.lottolifecycle.viewmodel.SharedViewModel

class LottoFragment : BaseFragment<FragmentLottoBinding, SharedViewModel>(R.layout.fragment_lotto) {

    private val mSharedViewModel : SharedViewModel by activityViewModels()

    override fun initDatabinding() {
        viewmodel = mSharedViewModel
    }

    override fun initFragment() {
        addOnClick()
    }

    private fun addOnClick() = with(binding){

        // 로또 번호 뽑기
        btnGetNumber.setOnClickListener {
            tvGetNumber.text = LottoUtils.setLottoNumbers(LottoUtils.drawNumbers())
        }

        // 로또 번호 다시 뽑기
        btnDrawNumberAgain.setOnClickListener {
            tvGetNumber.text = LottoUtils.setLottoNumbers(LottoUtils.drawNumbers())
        }

        // 로또 번호 확정 하기
        btnSetNumber.setOnClickListener {
            mSharedViewModel.setLottoNumber(LottoUtils.mLottoNumbers)
            removeFragment(this@LottoFragment)
        }

    }


}