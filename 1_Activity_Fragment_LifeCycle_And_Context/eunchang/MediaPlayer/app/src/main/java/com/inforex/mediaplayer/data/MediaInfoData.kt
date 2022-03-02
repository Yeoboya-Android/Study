package com.inforex.mediaplayer.data

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MediaInfoData(var thumbnailUri: Uri,
                    @SerializedName("tapeImgName") var thumbnailFileName: String = "",
                    @SerializedName("tapeFileName") var tapeFileName: String = "",
                    @SerializedName("tapeTitle", alternate = ["prevTitle"]) var title: String,
                    @SerializedName("chatName") var artistName: String,
                    var duration: Long,
                    var path: String
): Parcelable {
    val tapeImagePath get() = "https://photo2.club5678.com/tape/image/$thumbnailFileName?size=s&gifAniYn=y"
    val tapeFileUrl get() = "https://photo2.club5678.com/tape/audio/$tapeFileName"
}