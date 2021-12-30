package com.example.lottolifecycle.ui

import androidx.fragment.app.activityViewModels
import com.example.lottolifecycle.R
import com.example.lottolifecycle.base.BaseFragment
import com.example.lottolifecycle.databinding.FragmentResultBinding
import com.example.lottolifecycle.utils.LottoUtils
import com.example.lottolifecycle.viewmodel.SharedViewModel

class ResultFragment : BaseFragment<FragmentResultBinding, SharedViewModel>(R.layout.fragment_result) {

    private val mSharedViewModel : SharedViewModel by activityViewModels()

    override fun initDatabinding() {
        viewmodel = mSharedViewModel
    }

    override fun initFragment() {
            setData()
            addOnClick()
    }

    private fun setData() = with(binding){

        // 내 번호
        val myNumbers = mSharedViewModel.getLottoNumber()
        tvResultMyNumbers.text = LottoUtils.setLottoNumbers(myNumbers)

        // 당첨 번호
        val resultNumbers = LottoUtils.drawNumbers()
        tvResultNumbers.text = LottoUtils.setLottoNumbers(resultNumbers)

        // 등수 매기기
        val match = LottoUtils.winPrize(myNumbers, resultNumbers)
        tvMyLottoResult.text = LottoUtils.lottoResult(match)

    }

    private fun addOnClick() = with(binding) {

        btnBackToMain.setOnClickListener {
            mSharedViewModel.setLottoNumber(emptyList())
            removeFragment(this@ResultFragment)
        }

    }

}