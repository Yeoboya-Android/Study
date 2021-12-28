package com.lifecycletester.m.app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseFragment
import com.lifecycletester.m.app.databinding.FragmentChatBinding
import com.lifecycletester.m.app.ui.fragment.chat.ChatListAdapter
import java.io.Serializable

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(), ChatNavigator
{
    /*********************************************************/
    override val mViewModel: ChatViewModel by lazy { ViewModelProvider(this).get(ChatViewModel::class.java) }

    private lateinit var m_navigator: MainNavigator
    /*********************************************************/

    @LayoutRes
    override fun getLayoutResourceId() = R.layout.fragment_chat

    override fun initDataBinding(){
        mBinding!!.viewModel = mViewModel
        mBinding!!.lifecycleOwner = viewLifecycleOwner
    }

    override fun initView(){
        m_navigator = activity as MainNavigator

        initChatView()

        mViewModel.init(m_navigator, this, lifecycle)
    }

    fun initChatView(){
        mBinding!!.chatList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = ChatListAdapter(requireContext())
            //(adapter as ChatListAdapter).addChatList(mViewModel.m_chatList)
            //scrollToPosition(mViewModel.m_chatList.size-1)
        }
    }

    override fun onChatMsg(_msg: String) {
        (mBinding!!.chatList.adapter as ChatListAdapter).addChatData(_msg)
        mBinding!!.chatList.scrollToPosition(mViewModel.m_chatList.size-1)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("frag", "ChatFragment::onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("frag", "ChatFragment::onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

        Log.d("frag", "ChatFragment::onCreateView")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("frag", "ChatFragment::onViewCreated")
    }

    override fun onStart() {
        super.onStart()

        Log.d("frag", "ChatFragment::onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("frag", "ChatFragment::onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("frag", "ChatFragment::onPause")
    }

    override fun onStop() {
        super.onStop()

        Log.d("frag", "ChatFragment::onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("frag", "ChatFragment::onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("frag", "ChatFragment::onDestroy")
    }

    override fun onDetach() {
        super.onDetach()

        Log.d("frag", "ChatFragment::onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.d("frag", "ChatFragment::onSaveInstanceState")

        mBinding?.let{
            outState.putSerializable("chatList", (it.chatList.adapter as ChatListAdapter).getChatList() as Serializable)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Log.d("frag", "ChatFragment::onViewStateRestored")

        savedInstanceState?.let{
            val aaa = it.getSerializable("chatList") as MutableList<String>

            mBinding!!.chatList.let {_chatList->
                (_chatList.adapter as ChatListAdapter).addChatList(aaa)
                _chatList.scrollToPosition(aaa.size-1)
            }
        }
    }
}