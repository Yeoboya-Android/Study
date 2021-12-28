package com.lifecycletester.m.app.ui.fragment.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecycletester.m.app.R

class ChatListAdapter(val context : Context) : RecyclerView.Adapter<ChatViewHolder>()
{
    /*********************************************************/
    var m_chatData = mutableListOf<String>()
    /*********************************************************/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_msg, parent, false)
        return ChatViewHolder(parent.context, v)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int){
        val chatItem = m_chatData[holder.adapterPosition]
        val viewModel = ItemChatMsgViewModel()
        holder.mBinding?.viewModel = viewModel

        viewModel.initData(chatItem)
    }

    override fun getItemCount(): Int{
        return m_chatData.size
    }

    fun getChatList() = m_chatData

    fun addChatData(_chatData : String){
        m_chatData.add(_chatData)
        notifyItemInserted(m_chatData.size-1)
    }

    fun addChatList(_chatList : MutableList<String>){
        m_chatData = _chatList.toMutableList()
        notifyDataSetChanged()
    }

    fun clearData(){
        m_chatData.clear()
        notifyDataSetChanged()
    }
}