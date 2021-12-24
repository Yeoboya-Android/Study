package github.sun5066.lifecycle.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import github.sun5066.data.model.ImageData
import github.sun5066.lifecycle.util.asLiveData

class DetailViewModel(app: Application) : BaseViewModel(app) {

    private val _imageData = MutableLiveData<ImageData>()
    val imageData = _imageData.asLiveData()

    fun setImageData(imageData: ImageData) {
        _imageData.value = imageData
    }
}