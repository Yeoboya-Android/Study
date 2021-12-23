package com.example.lifecyclestudy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.lifecyclestudy.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    lateinit var mBinding: FragmentFirstBinding
    /**
     * activityViewModels()
     * 이를 통해 생성된 뷰모델은 액티비티의 뷰모델에 접근할 수 있다.
     * 액티비티 생명주기를 따라가는 액티비티 뷰모델을 프래그먼트에서 공통으로 쓸 수 있다.
     * 액티비티와 프래그먼트 간 데이터 공유가 필요할 때 사용 가능
     * Fragment.onAttach() 이후 사용 가능
     * */
    private val mViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container,false)
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = mViewModel
        }

        return mBinding.root
    }

}