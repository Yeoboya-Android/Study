package com.soo.screenrotate.faceunity

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.util.Log
import com.faceunity.nama.FURenderer
import com.faceunity.nama.utils.CameraUtils
import com.soo.screenrotate.utils.TextureIdHelp
import io.agora.capture.framework.modules.channels.VideoChannel.ChannelContext
import io.agora.capture.framework.modules.processors.IPreprocessor
import io.agora.capture.video.camera.VideoCaptureFrame


class PreprocessorFaceUnity(private val mContext: Context) : IPreprocessor {
    var fURenderer: FURenderer? = null
        private set
    private var mEnabled = true
    override fun onPreProcessFrame(
        outFrame: VideoCaptureFrame,
        context: ChannelContext
    ): VideoCaptureFrame {
        if (fURenderer == null || !mEnabled) {
            return outFrame
        }
        if (needCapture) {
            val currentTime = System.currentTimeMillis()
            val textureIdHelp = TextureIdHelp()
            val pre: Bitmap = textureIdHelp.FrameToBitmap(outFrame, true)
            textureIdHelp.saveBitmap2Gallery(mContext, pre, currentTime, "PRE")
            textureIdHelp.release()
            // process this frame
            outFrame.textureId = fURenderer!!.onDrawFrameDualInput(
                outFrame.image,
                outFrame.textureId, outFrame.format.width,
                outFrame.format.height
            )

            // The texture is transformed to texture2D by beauty module.
            outFrame.format.texFormat = GLES20.GL_TEXTURE_2D
            pre.recycle()
            val after: Bitmap = textureIdHelp.FrameToBitmap(outFrame, false)
            textureIdHelp.saveBitmap2Gallery(mContext, after, currentTime, "AFTER")
            after.recycle()
            textureIdHelp.release()
            needCapture = false
        } else {
            // process this frame
            outFrame.textureId = fURenderer!!.onDrawFrameDualInput(
                outFrame.image,
                outFrame.textureId, outFrame.format.width,
                outFrame.format.height
            )

            // The texture is transformed to texture2D by beauty module.
            outFrame.format.texFormat = GLES20.GL_TEXTURE_2D
        }
        return outFrame
    }

    override fun initPreprocessor() {
        // only call once when app launched
        Log.e(TAG, "initPreprocessor: ")
        fURenderer = FURenderer.Builder(mContext)
            .setInputTextureType(FURenderer.INPUT_TEXTURE_EXTERNAL_OES)
            .setCameraFacing(FURenderer.CAMERA_FACING_FRONT)
            .setInputImageOrientation(CameraUtils.getCameraOrientation(FURenderer.CAMERA_FACING_FRONT))
            .build()
        //        mFURenderer.onSurfaceCreated();
    }

    override fun enablePreProcess(enabled: Boolean) {
        mEnabled = enabled
    }

    override fun releasePreprocessor(context: ChannelContext) {
        // not called
        Log.d(TAG, "releasePreprocessor: ")
        //这个可以不写，在FUChatActivity中添加了
        if (fURenderer != null) {
            fURenderer?.onSurfaceDestroyed()
        }
    }

    companion object {
        private val TAG = PreprocessorFaceUnity::class.java.simpleName

        @Volatile
        var needCapture = false
    }
}
