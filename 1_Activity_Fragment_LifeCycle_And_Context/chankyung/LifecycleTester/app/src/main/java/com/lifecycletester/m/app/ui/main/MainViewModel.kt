package com.lifecycletester.m.app.ui.main

import android.view.View
import com.lifecycletester.m.app.MainNavigator
import com.lifecycletester.m.app.R
import com.lifecycletester.m.app.base.BaseViewModel

class MainViewModel : BaseViewModel()
{
    lateinit var m_navigator : MainNavigator
    val onClickListener = View.OnClickListener{
        when(it.id)
        {
            R.id.create_gps->{
                m_navigator.createGpsFragment()
            }
            R.id.replace_gps->{
                m_navigator.replaceGpsFragment()
            }

            R.id.create_chat->{
                m_navigator.createChatFragment()
            }
            R.id.replace_chat->{
                m_navigator.replaceChatFragment()
            }

            R.id.create_sub->{
                m_navigator.createSubActivity()
            }
            R.id.remove_frag->{
                m_navigator.removeFragment()
            }
        }
    }

    fun init(_navigator : MainNavigator){
        m_navigator = _navigator
    }
}