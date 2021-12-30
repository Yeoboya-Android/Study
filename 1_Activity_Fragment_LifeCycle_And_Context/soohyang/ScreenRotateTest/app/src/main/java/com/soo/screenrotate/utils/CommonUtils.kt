package com.soo.screenrotate.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

object CommonUtils {

    /** LiveData 2개 이상 관찰 */
    // 2개의 LiveData 를 관찰하여 LiveData 갱신
    fun <A, B> LiveData<A>.combine(other: LiveData<B>): PairLiveData<A, B> {
        return PairLiveData(this, other)
    }

    class PairLiveData<A, B>(first: LiveData<A>, second: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
        init {
            addSource(first) { value = it to second.value }
            addSource(second) { value = first.value to it }
        }
    }
}