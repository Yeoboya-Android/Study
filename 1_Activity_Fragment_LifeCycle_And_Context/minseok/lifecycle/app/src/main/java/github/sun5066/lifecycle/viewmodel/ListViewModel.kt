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

    private val _imageList = MutableStateFlow(listOf<ImageData>())
    val imageList = _imageList.asStateFlow()

    init {
        setImageList()
    }

    private fun setImageList() {
        GetImageDataUseCase(applicationContext).invoke()
            .flowOn(Dispatchers.IO)
            .onEach {
                delay(1)
                val list = _imageList.value.toMutableList()
                list.add(it)
                _imageList.value = list
            }.launchIn(viewModelScope)
    }
}