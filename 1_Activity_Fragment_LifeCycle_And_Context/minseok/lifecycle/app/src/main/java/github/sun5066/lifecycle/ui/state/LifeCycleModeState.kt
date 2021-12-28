package github.sun5066.lifecycle.ui.state

/**
 * Detatil View가 열릴때 어떤 방식으로 열릴지를 판단하는 상태
 * */
sealed class LifeCycleModeState {
    object UnInit : LifeCycleModeState()
    object Default : LifeCycleModeState()
    object BackStack : LifeCycleModeState()
    object Dialog : LifeCycleModeState()
    object NewActivity : LifeCycleModeState()
}