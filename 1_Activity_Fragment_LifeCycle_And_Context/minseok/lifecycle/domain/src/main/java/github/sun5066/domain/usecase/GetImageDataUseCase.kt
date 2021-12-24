package github.sun5066.domain.usecase

import android.content.Context
import github.sun5066.data.repository.ImageDataRepository

class GetImageDataUseCase(private val context: Context) {

    operator fun invoke() = ImageDataRepository(context).selectAll()
}