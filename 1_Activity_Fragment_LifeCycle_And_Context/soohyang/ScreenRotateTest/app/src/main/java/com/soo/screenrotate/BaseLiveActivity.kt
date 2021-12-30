package com.soo.screenrotate

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.soo.screenrotate.agora.RtcEngineEventHandler
import io.agora.capture.video.camera.CameraVideoManager
import io.agora.rtc.RtcEngine

abstract class RtcBasedActivity : AppCompatActivity(), RtcEngineEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStart() {
        super.onStart()
        addRtcHandler(this)
    }

    override fun onStop() {
        super.onStop()
        removeRtcHandler(this)
    }

    protected fun application(): ScreenRotateApplication {
        return application as ScreenRotateApplication
    }

    protected fun rtcEngine(): RtcEngine? {
        return application().rtcEngine()
    }

    protected fun videoManager(): CameraVideoManager? {
        return application().videoManager()
    }

    private fun addRtcHandler(handler: RtcEngineEventHandler) {
        application().addRtcHandler(handler)
    }

    private fun removeRtcHandler(handler: RtcEngineEventHandler) {
        application().removeRtcHandler(handler)
    }
}