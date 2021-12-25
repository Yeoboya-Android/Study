package com.soo.screenrotate

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.faceunity.nama.FURenderer
import com.soo.screenrotate.agora.AgoraConfig
import com.soo.screenrotate.agora.RtcEngineEventHandler
import com.soo.screenrotate.agora.RtcEngineEventHandlerProxy
import com.soo.screenrotate.faceunity.PreprocessorFaceUnity
import dagger.hilt.android.HiltAndroidApp
import io.agora.capture.video.camera.CameraVideoManager
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine

@HiltAndroidApp
class ScreenRotateApplication : Application() {

    private var mVideoManager: CameraVideoManager? = null
    private var mRtcEngine: RtcEngine? = null
    private var mRtcEventHandler: RtcEngineEventHandlerProxy? = null

    override fun onCreate() {
        super.onCreate()

        initRtcEngine()
        initVideoCaptureAsync()
    }

    private fun initRtcEngine() {
        val appId = AgoraConfig.APP_ID
        if (TextUtils.isEmpty(appId)) {
            throw RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/")
        }
        mRtcEventHandler = RtcEngineEventHandlerProxy()
        try {
            mRtcEngine = RtcEngine.create(this, appId, mRtcEventHandler)
            mRtcEngine?.enableVideo()
            mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_GAME)
        } catch (e: Exception) {
            throw RuntimeException(
                """
                NEED TO check rtc sdk init fatal error
                ${Log.getStackTraceString(e)}
                """.trimIndent()
            )
        }
    }

    private fun initVideoCaptureAsync() {
        val application = applicationContext
        FURenderer.setup(application)
        mVideoManager = CameraVideoManager(
            application,
            PreprocessorFaceUnity(application)
        )
    }

    fun rtcEngine(): RtcEngine? {
        return mRtcEngine
    }

    fun addRtcHandler(handler: RtcEngineEventHandler?) {
        if (handler != null) {
            mRtcEventHandler?.addEventHandler(handler)
        }
    }

    fun removeRtcHandler(handler: RtcEngineEventHandler?) {
        if (handler != null) {
            mRtcEventHandler?.removeEventHandler(handler)
        }
    }

    fun videoManager(): CameraVideoManager? {
        return mVideoManager
    }
}