package github.sun5066.lifecycle.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import github.sun5066.data.model.ImageData
import github.sun5066.domain.usecase.GetImageDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class ListViewModel(
    app: Application
) : BaseViewModel(app) {

    private val _imageData = MutableStateFlow<ImageData?>(null)
    val imageData = _imageData.asStateFlow()

    init {
        setImageList()
    }

    private fun setImageList() {
        GetImageDataUseCase(applicationContext).invoke()
            .flowOn(Dispatchers.IO)
            .onEach {
                delay(30)
                _imageData.value = it
            }
            .launchIn(viewModelScope)
    }
}