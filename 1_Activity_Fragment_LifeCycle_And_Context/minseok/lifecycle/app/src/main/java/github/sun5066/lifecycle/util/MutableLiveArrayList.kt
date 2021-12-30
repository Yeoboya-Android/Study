package github.sun5066.lifecycle.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableLiveArrayList<T> : MutableLiveData<ArrayList<T>>(arrayListOf<T>()) {

    fun add(item: T) {
        val items = value
        items?.add(item)
        value = items
    }

    fun addAll(list: List<T>) {
        val items = value
        items?.addAll(list)
        value = items
    }

    fun clear(notify: Boolean) {
        val items = value
        items?.clear()
        if (notify) value = items
    }

    fun remove(item: T) {
        val items = value
        items?.remove(item)
        value = items
    }

    fun removeAt(index: Int) {
        val items = value
        items?.removeAt(index)
        value = items
    }

    fun removeLast() = value?.takeIf { it.isNotEmpty() }?.apply {
        val items = this
        val lastIndex = items.lastIndex
        items.removeAt(lastIndex)
        value = items
    } ?: throw NoSuchElementException("list is Empty!!")

    fun last(defaultValue: T): T = value?.takeIf { it.isNotEmpty() }?.last() ?: defaultValue

}
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>
