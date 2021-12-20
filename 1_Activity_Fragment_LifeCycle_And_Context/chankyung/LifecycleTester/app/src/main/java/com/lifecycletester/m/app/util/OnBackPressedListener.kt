package com.lifecycletester.m.app.util

interface OnBackPressedListener
{
    fun onBackPressed()
    // onBackPressed() 다음에 super.onBackPressed()을 수행 할지에 대한 flag
    val onSuperBackPressed: Boolean
}