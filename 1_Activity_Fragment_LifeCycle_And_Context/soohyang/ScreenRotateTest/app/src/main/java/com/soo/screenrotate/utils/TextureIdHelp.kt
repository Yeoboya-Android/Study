package com.soo.screenrotate.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.opengl.GLES20
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.NonNull
import io.agora.capture.video.camera.VideoCaptureFrame
import io.agora.rtc.gl.GlRectDrawer
import io.agora.rtc.gl.GlTextureFrameBuffer
import io.agora.rtc.gl.RendererCommon
import io.agora.rtc.gl.VideoFrame.TextureBuffer
import java.io.*
import java.nio.ByteBuffer

class TextureIdHelp {

    private var bitmapTextureFramebuffer: GlTextureFrameBuffer? = null
    private var textureDrawer: GlRectDrawer? = null

    fun FrameToBitmap(frame: VideoCaptureFrame, raw: Boolean): Bitmap {
        val matrix = RendererCommon.convertMatrixToAndroidGraphicsMatrix(frame.textureTransform)
        val type: TextureBuffer.Type
        type = if (raw) TextureBuffer.Type.OES else TextureBuffer.Type.RGB
        return textureIdToBitmap(
            frame.format.width,
            frame.format.height,
            frame.rotation,
            type,
            frame.textureId,
            matrix
        )
    }

    fun textureIdToBitmap(
        width: Int,
        height: Int,
        rotation: Int,
        type: TextureBuffer.Type,
        textureId: Int,
        transformMatrix: Matrix?
    ): Bitmap {
        if (textureDrawer == null) {
            textureDrawer = GlRectDrawer()
        }
        if (bitmapTextureFramebuffer == null) {
            bitmapTextureFramebuffer = GlTextureFrameBuffer(GLES20.GL_RGBA)
        }
        val frameWidth = if (rotation % 180 == 0) width else height
        val frameHeight = if (rotation % 180 == 0) height else width
        bitmapTextureFramebuffer!!.setSize(frameWidth, frameHeight)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, bitmapTextureFramebuffer!!.frameBufferId)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        val renderMatrix = Matrix()
        renderMatrix.preTranslate(0.5f, 0.5f)
        renderMatrix.preRotate(rotation.toFloat() + 270) // need rotate 180 from texture to bitmap
        renderMatrix.preTranslate(-0.5f, -0.5f)
        renderMatrix.postConcat(transformMatrix)
        val finalGlMatrix = RendererCommon.convertMatrixFromAndroidGraphicsMatrix(renderMatrix)
        if (type == TextureBuffer.Type.OES) {
            textureDrawer!!.drawOes(
                textureId,
                finalGlMatrix,
                frameWidth,
                frameHeight,
                0,
                0,
                frameWidth,
                frameHeight
            )
        } else {
            textureDrawer!!.drawRgb(
                textureId,
                finalGlMatrix,
                frameWidth,
                frameHeight,
                0,
                0,
                frameWidth,
                frameHeight
            )
        }
        val bitmapBuffer = ByteBuffer.allocateDirect(frameWidth * frameHeight * 4)
        GLES20.glViewport(0, 0, frameWidth, frameHeight)
        GLES20.glReadPixels(
            0, 0, frameWidth, frameHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bitmapBuffer
        )
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        val mBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888)
        mBitmap.copyPixelsFromBuffer(bitmapBuffer)
        return mBitmap
    }

    fun release() {
        if (textureDrawer != null) {
//            textureDrawer.release();
            textureDrawer = null
        }
        if (bitmapTextureFramebuffer != null) {
            bitmapTextureFramebuffer!!.release()
            bitmapTextureFramebuffer = null
        }
    }

    fun saveBitmap2Gallery(context: Context, bm: Bitmap, currentTime: Long, tag: String) {
        // name the file
        val imageFileName = "IMG_AGORA_" + currentTime + "_" + tag + ".jpg"
        val imageFilePath: String
        //        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//            imageFilePath = Environment.DIRECTORY_PICTURES + File.separator + "Agora" + File.separator;
//        else
        imageFilePath =
            (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                    + File.separator + "Agora" + File.separator)

        // write to file
        val outputStream: OutputStream
        val resolver = context.contentResolver
        val newScreenshot = ContentValues()
        val insert: Uri?
        newScreenshot.put(MediaStore.Images.ImageColumns.DATE_ADDED, currentTime)
        newScreenshot.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, imageFileName)
        newScreenshot.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg")
        newScreenshot.put(MediaStore.Images.ImageColumns.WIDTH, bm.width)
        newScreenshot.put(MediaStore.Images.ImageColumns.HEIGHT, bm.height)
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                newScreenshot.put(MediaStore.Images.ImageColumns.RELATIVE_PATH,imageFilePath);
//            }else{
            // make sure the path is existed
            val imageFileDir = File(imageFilePath)
            if (!imageFileDir.exists()) {
                val mkdir = imageFileDir.mkdirs()
                if (!mkdir) {
                    Toast.makeText(
                        context,
                        "save failed, error: cannot create folder. Make sure app has the permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }
            newScreenshot.put(MediaStore.Images.ImageColumns.DATA, imageFilePath + imageFileName)
            newScreenshot.put(MediaStore.Images.ImageColumns.TITLE, imageFileName)
            //            }

            // insert a new image
            insert = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newScreenshot)
            // write data
            outputStream = resolver.openOutputStream(insert!!)!!
            bm.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
            outputStream.flush()
            outputStream.close()
            newScreenshot.clear()
            newScreenshot.put(MediaStore.Images.ImageColumns.SIZE, File(imageFilePath).length())
            resolver.update(insert, newScreenshot, null, null)
            Toast.makeText(context, "save success, you can view it in gallery", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(context, "save failed, error: " + e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun saveBitmap(@NonNull file: File?, @NonNull bmp: Bitmap) {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}