package github.sun5066.lifecycle.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import github.sun5066.lifecycle.ui.state.LastViewState
import github.sun5066.lifecycle.ui.state.LifeCycleModeState
import github.sun5066.lifecycle.util.asLiveData

class MainViewModel(app: Application) : BaseViewModel(app) {

    private val _lifeCycleState = MutableLiveData<LifeCycleModeState>(LifeCycleModeState.Default)
    val lifeCycleState = _lifeCycleState.asLiveData()

    private val _lastViewState = MutableLiveData<LastViewState>(LastViewState.ListFragment)
    val lastViewState = _lastViewState.asLiveData()

    fun setLifeCycleModeState(state: LifeCycleModeState) {
        _lifeCycleState.value = state
    }

    fun setLastViewState(state: LastViewState) {
        _lastViewState.value = state
    }
}