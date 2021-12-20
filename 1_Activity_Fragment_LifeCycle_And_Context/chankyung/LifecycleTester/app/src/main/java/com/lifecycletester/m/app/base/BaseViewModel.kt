package com.lifecycletester.m.app.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel()
{
    /** initialize */
    protected var m_baseNavigator: BaseNavigator? = null

    /** RxJava Disposable 관리 */
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun removeDisposable(disposable: Disposable){
        compositeDisposable.remove(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()

        super.onCleared()
    }
}