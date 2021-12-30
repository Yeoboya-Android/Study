package com.example.lottolifecycle.viewmodel

class SharedViewModel : BaseViewModel(){

    private var mLottoNumber : List<Int> = emptyList()
    fun setLottoNumber(lottoNumber : List<Int>) {mLottoNumber = lottoNumber.toMutableList()}
    fun getLottoNumber() = mLottoNumber

}