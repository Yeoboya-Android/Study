package com.inforex.mediaplayer.data

import com.google.gson.annotations.SerializedName

data class BaseData<T>(@SerializedName("code") val code: String,
                       @SerializedName("message") val message: String,
                       @SerializedName("data") val data: T)

class DataList<T> : ArrayList<T>() {

    override fun toString(): String {
        val it: Iterator<T> = iterator()
        if (!it.hasNext()) return "[size = 0, 결과 없음.]"

        val sb = StringBuilder()
        sb.append("result size: $size\n")

        var index = 0
        while (true) {
            sb.append("[${index++}]: ")
            val e: T = it.next()
            sb.append(if (e === this) "(this Collection)" else e)
            if (!it.hasNext()) return sb.append("\nEnd.").toString()
            sb.append("\n")
        }
    }
}