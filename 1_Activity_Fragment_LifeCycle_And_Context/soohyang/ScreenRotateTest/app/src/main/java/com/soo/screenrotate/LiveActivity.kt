package com.soo.screenrotate

import android.content.Intent
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.faceunity.nama.FURenderer
import com.soo.screenrotate.agora.AgoraConfig
import com.soo.screenrotate.agora.RtcEngineEventHandler
import com.soo.screenrotate.agora.RtcVideoConsumer
import com.soo.screenrotate.databinding.ActivityLiveBinding
import com.soo.screenrotate.faceunity.PreprocessorFaceUnity
import com.soo.screenrotate.viewmodels.LiveViewModel
import io.agora.capture.video.camera.CameraVideoManager
import io.agora.capture.video.camera.Constant
import io.agora.capture.video.camera.VideoCapture.VideoCaptureStateListener
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.BeautyOptions
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import java.util.concurrent.CountDownLatch
import kotlin.math.abs

class LiveActivity : RtcBasedActivity(), RtcEngineEventHandler, SensorEventListener {

    private lateinit var mBinding: ActivityLiveBinding
    private val mViewModel: LiveViewModel by viewModels()

    private var mVideoManager: CameraVideoManager? = null
    private var mFURenderer: FURenderer? = null

    private var mOrientation = "portrait"
    private var mRemoteUid = -1
    private var mFinished = false
    private val mCameraFace = FURenderer.CAMERA_FACING_FRONT
    private var mSensorManager: SensorManager? = null

    companion object {
        val KEY_IS_BROADCASTER = "key_is_broadcaster"
        val KEY_ORIENTATION = "key_orientation"
        val SAVE_KEY_REMOTE_USER_ID = "save_key_remote_user_id"
        val SAVE_KEY_ORIENTATION = "save_key_orientation"
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_rotate -> {
                Log.d("soohyangA", "btn_rotate")

                // 테스트를 위해 강제 지정
                if(mOrientation != "portrait") {
                    mOrientation = "portrait"
                    val convertedOrientation = when (mOrientation) {
                        "landscape" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        "portrait" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                        "adaptive" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
                        else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
                    }
                    requestedOrientation = convertedOrientation
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("soohyangA", "onCreate")

        super.onCreate(savedInstanceState)

        val isBroadcaster = intent.getBooleanExtra(KEY_IS_BROADCASTER, false)
        mOrientation = intent.getStringExtra(KEY_ORIENTATION) ?: "adaptive"

        if (savedInstanceState != null) {
            mRemoteUid = savedInstanceState.getInt(SAVE_KEY_REMOTE_USER_ID, -1)
            mOrientation = savedInstanceState.getString(SAVE_KEY_ORIENTATION, mOrientation)
        }

        mBinding = DataBindingUtil.setContentView<ActivityLiveBinding>(this, R.layout.activity_live)
        mBinding.apply {
            lifecycleOwner = this@LiveActivity
            viewModel = mViewModel.apply {
                setIsBroadcaster(isBroadcaster)
            }
            clickListener = onClickListener
        }

        val convertedOrientation = when (mOrientation) {
            "landscape" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            "portrait" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            "adaptive" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
            else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
        requestedOrientation = convertedOrientation
        initRoom(isBroadcaster, mOrientation)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // 방송 청취중에 화면이 회전되면 view가 제거되기 때문에 saveInstanceState가 있는 경우 다시 그려준다.
        // saveInstanceState하는 다른 경우 확인 필요
        if(mRemoteUid != -1) {
            runOnUiThread {
                removeRemoteView()
                setRemoteVideoView(mRemoteUid)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("soohyangA", "onNewIntent")

        super.onNewIntent(intent)

        Log.w("soohyangA", "onNewIntent $intent")
        //!intent.getBooleanExtra(KEY_IS_PIN_MODE_SET, false)
    }

    private fun initRoom(isBroadcaster: Boolean, orientation: String) {
        initVideoModule()
        rtcEngine()!!.setVideoSource(RtcVideoConsumer())
        joinChannel(isBroadcaster, orientation)
    }

    private fun initVideoModule() {
        mVideoManager = videoManager()
        mVideoManager?.setCameraStateListener(object : VideoCaptureStateListener {
            override fun onFirstCapturedFrame(width: Int, height: Int) {
                Log.i(
                    "soohyangA",
                    "onFirstCapturedFrame: " + width + "x" + height
                )
            }

            override fun onCameraCaptureError(error: Int, msg: String) {
                Log.i(
                    "soohyangA",
                    "onCameraCaptureError: error:$error $msg"
                )
                if (mVideoManager != null) {
                    // When there is a camera error, the capture should
                    // be stopped to reset the internal states.
                    mVideoManager!!.stopCapture()
                }
            }

            override fun onCameraClosed() {}
        })

        mFURenderer = (mVideoManager?.preprocessor as? PreprocessorFaceUnity)?.fURenderer
        mFURenderer?.also {
            mBinding.fuView.setModuleManager(it)
            it.setOnTrackStatusChangedListener { type, status ->
                val faceDetect = status > 0
                val description =
                    if (type == FURenderer.TRACK_TYPE_FACE) R.string.toast_not_detect_face else R.string.toast_not_detect_face_or_body
                mViewModel.setDetectFace(faceDetect, description)
            }
        }

        mVideoManager?.apply {
            setPictureSize(
                AgoraConfig.CAPTURE_WIDTH,
                AgoraConfig.CAPTURE_HEIGHT
            )
            setFrameRate(AgoraConfig.CAPTURE_FRAME_RATE)
            setFacing(Constant.CAMERA_FACING_FRONT)
            setLocalPreviewMirror(Constant.MIRROR_MODE_AUTO)
            setLocalPreview(mBinding.localVideoView)
        }
    }

    private fun joinChannel(isBroadcaster: Boolean, orientation: String) {
        val convertedOrientation = when (orientation) {
            "landscape" -> VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE
            "portrait" -> VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            "adaptive" -> VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
            else -> VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
        }
        rtcEngine()!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                convertedOrientation
            )
        )
        rtcEngine()!!.setClientRole(if (isBroadcaster) Constants.CLIENT_ROLE_BROADCASTER else Constants.CLIENT_ROLE_AUDIENCE)
        rtcEngine()!!.enableLocalAudio(false)
        rtcEngine()!!.setBeautyEffectOptions(true, BeautyOptions())
        rtcEngine()!!.joinChannel(AgoraConfig.TOKEN, AgoraConfig.CHANNEL_NAME, null, 0)
    }

    override fun onStart() {
        Log.d("soohyangA", "onStart")

        super.onStart()
        val sensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        mVideoManager?.startCapture()
        mFURenderer?.queueEvent { mFURenderer?.onSurfaceCreated() }
    }

    override fun finish() {
        Log.d("soohyangA", "finish")


        mFinished = true
        val countDownLatch = CountDownLatch(1)
        mFURenderer?.queueEvent {
            mFURenderer?.onSurfaceDestroyed()
            countDownLatch.countDown()
        }
        try {
            countDownLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        mVideoManager?.stopCapture()
        mVideoManager?.setCameraStateListener(null)
        mFURenderer?.setOnTrackStatusChangedListener(null)
        rtcEngine()!!.leaveChannel()
        super.finish()
    }

    override fun onStop() {
        Log.d("soohyangA", "onStop")

        super.onStop()
        if (!mFinished) {
            mVideoManager?.stopCapture()
        }
        mSensorManager?.unregisterListener(this)
    }

    override fun onPause() {
        Log.d("soohyangA", "onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.d("soohyangA", "onResume")
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("soohyangA", "onSaveInstanceState")
        outState.putInt(SAVE_KEY_REMOTE_USER_ID, mRemoteUid)
        outState.putString(SAVE_KEY_ORIENTATION, mOrientation)
    }

    override fun onDestroy() {
        Log.d("soohyangA", "onDestroy")
        super.onDestroy()
    }


    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        Log.d("soohyangA", "onJoinChannelSuccess $uid $mRemoteUid")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        Log.d("soohyangA", "onUserOffline $uid")
        runOnUiThread { onRemoteUserLeft() }
    }

    private fun onRemoteUserLeft() {
        mRemoteUid = -1
        removeRemoteView()
    }

    private fun removeRemoteView() {
        mBinding.remoteVideoView.removeAllViews()
    }


    override fun onUserJoined(uid: Int, elapsed: Int) {
        Log.d("soohyangA", "onUserJoined $uid")
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        Log.d("soohyangA", "onRemoteVideoStateChanged $uid $state $reason")
        if (mRemoteUid == -1 && state == Constants.REMOTE_VIDEO_STATE_DECODING) {
            runOnUiThread {
                mRemoteUid = uid
                setRemoteVideoView(uid)
            }
        }
    }

    private fun setRemoteVideoView(uid: Int) {
        Log.d("soohyangA", "setRemoteVideoView")
        val surfaceView = RtcEngine.CreateRendererView(this)
        rtcEngine()!!.setupRemoteVideo(
            VideoCanvas(
                surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid
            )
        )
        mBinding.remoteVideoView.addView(surfaceView)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            if (abs(x) > 3 || abs(y) > 3) {
                if (abs(x) > abs(y)) {
                    mFURenderer?.onDeviceOrientationChanged(if (x > 0) 0 else 180)
                } else {
                    mFURenderer?.onDeviceOrientationChanged(if (y > 0) 90 else 270)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}