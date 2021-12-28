package com.lifecycletester.m.app.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseViewModel
import com.orhanobut.logger.Logger

class ChatViewModel : BaseViewModel()
{
    /*********************************************************/
    private val _chatText = MutableLiveData("")
    val chatText: LiveData<String> get() = _chatText

    private val _isChatMode = MutableLiveData(false)
    val isChatMode: LiveData<Boolean> get() = _isChatMode

    var m_curChatMsg = ""
    val m_chatList = mutableListOf<String>()

    lateinit var m_chatNavigator : ChatNavigator
    /*********************************************************/

    val onClickListener = View.OnClickListener {
        when(it.id)
        {
            R.id.btn_send->{
                m_chatList.add(m_curChatMsg)
                m_chatNavigator.onChatMsg(m_curChatMsg)

                m_curChatMsg = ""
                _chatText.value = ""

                //_isChatMode.value = false
            }
        }
    }

    val onFocusChangeListener = View.OnFocusChangeListener{_view, _hasFocus->
        Logger.i("$_hasFocus")
    }

    val onTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
            m_curChatMsg = s.toString()
        }
    }

    fun init(_navigator : MainNavigator, _chatNavigator : ChatNavigator, _lifeCycle : Lifecycle){
        m_chatNavigator = _chatNavigator
        _chatText.value = m_curChatMsg
    }
}