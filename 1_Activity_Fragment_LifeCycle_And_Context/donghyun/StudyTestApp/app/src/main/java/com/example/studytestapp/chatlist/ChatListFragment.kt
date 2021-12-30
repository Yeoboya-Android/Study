package com.example.studytestapp.chatlist

import androidx.fragment.app.viewModels
import com.example.studytestapp.BaseFragment
import com.example.studytestapp.R
import com.example.studytestapp.databinding.FragmentChatlistBinding

class ChatListFragment : BaseFragment<FragmentChatlistBinding, ChatListViewModel>() {
    override fun getLayoutResourceId() = R.layout.fragment_chatlist
    override val mViewModel: ChatListViewModel by viewModels()

    override fun initDataBinding() {
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initView() {
        mViewModel.init()
    }

}