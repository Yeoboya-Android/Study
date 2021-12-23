package com.example.lottolifecycle.utils

import java.lang.StringBuilder
import kotlin.random.Random

object LottoUtils{

    val mLottoNumbers = mutableListOf<Int>()

    // 로또 번호 뽑는 로직
    fun drawNumbers() : List<Int>{

        if(mLottoNumbers.size > 0){
            mLottoNumbers.clear()
        }

        while(mLottoNumbers.size < 6) {
            val randomNumber = Random.nextInt(1, 46)

            if (mLottoNumbers.contains(randomNumber)) {
                continue
            }

            mLottoNumbers.add(randomNumber)
        }

        mLottoNumbers.sort()
        return mLottoNumbers
    }

    // 로또 번호 셋팅
    fun setLottoNumbers(numbers : List<Int>) : String{

        val result = StringBuilder()

        numbers.forEach {
            result.append("$it ,")
        }

        result.deleteCharAt(result.length.minus(1))

        return result.toString()
    }

    // 로또 번호 비교해서 등수 알려주기
    fun winPrize(myLottoNumbers : List<Int>, resultLottoNumbers : List<Int>) : Int{
        var match = 0

        myLottoNumbers.forEach { lottoNum ->
            val myNum = lottoNum

            resultLottoNumbers.forEach { resultNum ->
                if(myNum == resultNum){
                    match++
                }
            }
        }

        return match
    }

    // 로또 번호 맞는 갯수에 따라서 결과 return
    fun lottoResult(match : Int) : String{

        return when(match){
            6 -> "1등!"
            5 -> "2등!"
            4 -> "3등!"
            3 -> "4등!"
            2 -> "5등!"
            1 -> "6등!"
            0 -> "당첨되지 않았습니다..."
            else -> ""
        }
    }

}