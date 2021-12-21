package github.sun5066.data.source

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import github.sun5066.data.source.model.ImageData

object ImageSourceService  {

    private const val ID = MediaStore.Images.Media._ID
    private const val DISPLAY_NAME = MediaStore.Images.Media.DISPLAY_NAME

    fun selectAll(context: Context): List<ImageData> {
        val projection = arrayOf(ID, DISPLAY_NAME)
        val imageList = ArrayList<ImageData>()

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString()
                )

                imageList.add(
                    ImageData(id, displayName, contentUri)
                )
            }
        }

        return imageList
    }
}