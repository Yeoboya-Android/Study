package github.sun5066.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val id: Long,
    val displayName: String,
    val uri: Uri
) : Parcelable