package com.lifecycletester.m.app.ui.bindingadapter

import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lifecycletester.m.app.ui.fragment.chat.ChatListAdapter

object CommonBindingAdapter
{
    /****************** Edit Text *********************************/
    @BindingAdapter("onFocusChangeListener")
    @JvmStatic
    fun setOnFocusChangeListener(view: EditText, listener: View.OnFocusChangeListener?) {
        view.onFocusChangeListener = listener
    }

    @BindingAdapter("onTextChangeListener")
    @JvmStatic
    fun setOnTextChangeListener(view: EditText, listener: TextWatcher?) {
        view.addTextChangedListener(listener)
    }

    @BindingAdapter("focus")
    @JvmStatic
    fun changeFocus(view: EditText, flag: Boolean?) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        flag?.let {
            if (flag) {
                view.requestFocus()
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            } else {
                view.clearFocus()
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    /****************** Recycler View *********************************/
    @BindingAdapter("addChatData")
    @JvmStatic
    fun addChatData(view: RecyclerView, chatData: String?){
        chatData?.let{
            val adapter = view.adapter as ChatListAdapter
            adapter.addChatData(it)
        }
    }
}