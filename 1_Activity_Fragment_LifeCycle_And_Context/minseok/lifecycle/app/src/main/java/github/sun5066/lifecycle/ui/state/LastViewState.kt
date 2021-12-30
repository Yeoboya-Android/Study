package github.sun5066.lifecycle.ui.state

import github.sun5066.data.model.ImageData

sealed class LastViewState {
    object UnInitialize : LastViewState()
    object ListFragment : LastViewState()
    data class DetailFragment(val imageData: ImageData) : LastViewState()
    data class DetailDialog(val imageData: ImageData) : LastViewState()
}
