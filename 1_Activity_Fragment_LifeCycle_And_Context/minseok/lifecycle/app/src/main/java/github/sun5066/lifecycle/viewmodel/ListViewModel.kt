package github.sun5066.lifecycle.viewmodel

import android.app.Application
import github.sun5066.data.model.ImageData
import github.sun5066.domain.usecase.GetImageDataUseCase
import github.sun5066.lifecycle.util.MutableLiveArrayList
import github.sun5066.lifecycle.util.asLiveData

class ListViewModel(
    app: Application
) : BaseViewModel(app) {

    private val _imageList = MutableLiveArrayList<ImageData>()
    val imageList = _imageList.asLiveData()

    init {
        setImageList()
    }

    private fun setImageList() {
        val list = GetImageDataUseCase(applicationContext).invoke()
        _imageList.addAll(list)
    }

    fun refreshList() {
        setImageList()
    }

}