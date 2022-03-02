package com.inforex.mediaplayer.ui.base

import com.inforex.mediaplayer.data.BaseData
import com.inforex.mediaplayer.data.DataList
import com.inforex.mediaplayer.data.MediaInfoData
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface MediaApiInterface {

    @POST("api/tape/list")
    suspend fun getTapeList(@Body body: RequestBody): BaseData<DataList<MediaInfoData>>


}