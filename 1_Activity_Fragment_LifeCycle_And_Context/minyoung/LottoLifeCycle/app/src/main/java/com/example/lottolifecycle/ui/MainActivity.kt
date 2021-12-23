package com.example.lottolifecycle.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.lottolifecycle.R
import com.example.lottolifecycle.base.BaseActivity
import com.example.lottolifecycle.databinding.ActivityMainBinding
import com.example.lottolifecycle.utils.DialogUtils
import com.example.lottolifecycle.viewmodel.SharedViewModel

class MainActivity : BaseActivity<ActivityMainBinding, SharedViewModel>(R.layout.activity_main) {

    // viewmodel
    private val mSharedViewModel : SharedViewModel by viewModels()

    // fragment
    private var mLottoFragment : LottoFragment? = null
    private var mResultFragment : ResultFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBinding()
    }

    override fun initDataBinding(){
        viewmodel = mSharedViewModel
    }

    override fun initActivity() {

        addOnClick()

    }


    private fun addOnClick() = with(binding) {

        // 로또 번호 뽑기
        btnDrawNumber.setOnClickListener {
            if(null == mLottoFragment) mLottoFragment = LottoFragment()
            addFragment(R.id.fragment_container, mLottoFragment!!)
        }

        // 로또 결과 보기
        btnShowResult.setOnClickListener {

            if(mSharedViewModel.getLottoNumber().isNullOrEmpty()) {
                val dialog = DialogUtils(this@MainActivity)
                dialog.showSingleDialog("로또 번호 없음", "아직 번호가 확정되지 않았습니다", false)
            }else {
                if (null == mResultFragment) mResultFragment = ResultFragment()
                addFragment(R.id.fragment_container, mResultFragment!!)
            }
        }
    }


}