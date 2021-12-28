package com.example.studytestapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.studytestapp.widget.MaxLineEditText

object CommonBindingAdapter {
    @BindingAdapter("onClickListener")
    @JvmStatic
    fun setOnClickListener(view: View, listener: View.OnClickListener?) {
        view.setOnClickListener(listener)
    }

    @BindingAdapter("changeEditTextListener")
    @JvmStatic
    fun setChangeTextListener(view: MaxLineEditText, listener: MaxLineEditText.ChangeEditTextListener?) {
        listener?.let { view.setChangeTextListener(it) }
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
}