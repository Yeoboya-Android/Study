package com.soo.screenrotate.agora

import android.opengl.GLES20
import android.util.Log
import io.agora.capture.framework.modules.channels.ChannelManager
import io.agora.capture.framework.modules.channels.VideoChannel.ChannelContext
import io.agora.capture.framework.modules.consumers.IVideoConsumer
import io.agora.capture.video.camera.VideoCaptureFrame
import io.agora.capture.video.camera.VideoModule
import io.agora.rtc.mediaio.IVideoFrameConsumer
import io.agora.rtc.mediaio.IVideoSource
import io.agora.rtc.mediaio.MediaIO
import io.agora.rtc.video.AgoraVideoFrame

class RtcVideoConsumer : IVideoConsumer, IVideoSource {

    companion object {
        private val TAG = RtcVideoConsumer::class.java.simpleName
    }

    @Volatile
    private var mRtcConsumer: IVideoFrameConsumer? = null

    @Volatile
    private var mValidInRtc = false

    @Volatile
    private var mVideoModule: VideoModule = VideoModule.instance()

    private val mChannelId: Int = ChannelManager.ChannelID.CAMERA



    override fun onConsumeFrame(frame: VideoCaptureFrame, context: ChannelContext) {
        if (mValidInRtc) {
            val format =
                if (frame.format.texFormat == GLES20.GL_TEXTURE_2D) AgoraVideoFrame.FORMAT_TEXTURE_2D else AgoraVideoFrame.FORMAT_TEXTURE_OES
            if (mRtcConsumer != null) {
                mRtcConsumer!!.consumeTextureFrame(
                    frame.textureId, format,
                    frame.format.width, frame.format.height,
                    frame.rotation, frame.timestamp, frame.textureTransform
                )
            }
        }
    }

    override fun connectChannel(channelId: Int) {
        // Rtc transmission is an off-screen rendering procedure.
        val channel = mVideoModule.connectConsumer(
            this, channelId, IVideoConsumer.TYPE_OFF_SCREEN
        )
    }

    override fun disconnectChannel(channelId: Int) {
        mVideoModule.disconnectConsumer(this, channelId)
    }

    override fun setMirrorMode(mode: Int) {}
    override fun getDrawingTarget(): Any? {
        return null
    }

    override fun onMeasuredWidth(): Int {
        return 0
    }

    override fun onMeasuredHeight(): Int {
        return 0
    }

    override fun recycle() {}
    override fun getId(): String? {
        return null
    }

    override fun onInitialize(consumer: IVideoFrameConsumer): Boolean {
        Log.i(TAG, "onInitialize")
        mRtcConsumer = consumer
        return true
    }

    override fun onStart(): Boolean {
        Log.i(TAG, "onStart")
        connectChannel(mChannelId)
        mValidInRtc = true
        return true
    }

    override fun onStop() {
        mValidInRtc = false
        mRtcConsumer = null
    }

    override fun onDispose() {
        Log.i(TAG, "onDispose")
        mValidInRtc = false
        mRtcConsumer = null
        disconnectChannel(mChannelId)
    }

    override fun getBufferType(): Int {
        return MediaIO.BufferType.TEXTURE.intValue()
    }

    override fun getCaptureType(): Int {
        return MediaIO.CaptureType.CAMERA.intValue()
    }

    override fun getContentHint(): Int {
        return MediaIO.ContentHint.NONE.intValue()
    }
}
