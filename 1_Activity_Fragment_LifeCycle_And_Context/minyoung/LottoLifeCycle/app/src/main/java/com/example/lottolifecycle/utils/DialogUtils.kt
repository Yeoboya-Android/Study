package com.example.lottolifecycle.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.example.lottolifecycle.R

class DialogUtils(context: Context) {

    private val mDialog = Dialog(context)
    private lateinit var mTitle : TextView
    private lateinit var mDesc : TextView
    private lateinit var mBtn : Button

    fun showSingleDialog(title : String, message : String, cancellable : Boolean){

        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setContentView(R.layout.single_dialog)
        mDialog.setCancelable(cancellable)

        mTitle = mDialog.findViewById(R.id.tv_dialog_title)
        mTitle.text = title

        mDesc = mDialog.findViewById(R.id.tv_dialog_desc)
        mDesc.text = message

        mBtn = mDialog.findViewById(R.id.btn_dialog_close)
        mBtn.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()
    }


}