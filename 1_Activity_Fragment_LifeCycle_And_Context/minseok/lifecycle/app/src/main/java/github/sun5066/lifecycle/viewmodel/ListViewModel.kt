package github.sun5066.lifecycle.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import github.sun5066.data.model.ImageData
import github.sun5066.domain.usecase.GetImageDataUseCase
import github.sun5066.lifecycle.util.MutableLiveArrayList
import github.sun5066.lifecycle.util.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class ListViewModel(
    app: Application
) : BaseViewModel(app) {

    private val _imageList = MutableLiveArrayList<ImageData>()
    val imageList = _imageList.asLiveData()

    init {
        setImageList()
    }

    private fun setImageList() {
        GetImageDataUseCase(applicationContext).invoke()
            .flowOn(Dispatchers.IO)
            .onEach {
                delay(1)
                _imageList.add(it)
            }
            .launchIn(viewModelScope)
    }

    fun refreshList() {
        setImageList()
    }

}