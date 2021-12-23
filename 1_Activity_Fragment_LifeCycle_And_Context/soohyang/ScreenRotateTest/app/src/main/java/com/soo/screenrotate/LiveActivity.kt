package com.soo.screenrotate

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
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
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import java.util.concurrent.CountDownLatch
import kotlin.math.abs

class LiveActivity : RtcBasedActivity(), RtcEngineEventHandler, SensorEventListener {

    private lateinit var mBinding: ActivityLiveBinding
    private val mViewModel: LiveViewModel by viewModels()

    private var mVideoManager: CameraVideoManager? = null
    private var mFURenderer: FURenderer? = null

    private var mRemoteUid = -1
    private var mFinished = false
    private val mCameraFace = FURenderer.CAMERA_FACING_FRONT
    private var mSensorManager: SensorManager? = null

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            //R.id.btn_header_left -> findNavController().navigateUp()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityLiveBinding>(this, R.layout.activity_live)
        mBinding.apply {
            lifecycleOwner = this@LiveActivity
            viewModel = mViewModel
            clickListener = onClickListener
        }

        initUI()
        initRoom()

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val sdkVersion = RtcEngine.getSdkVersion()
    }

    private fun initUI() {
        initRemoteViewLayout()
    }

    private fun initRemoteViewLayout() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val params = mBinding.remoteVideoView.layoutParams as RelativeLayout.LayoutParams
        params.width = displayMetrics.widthPixels / 3
        params.height = displayMetrics.heightPixels / 3
        mBinding.remoteVideoView.layoutParams = params
    }

    private fun initRoom() {
        initVideoModule()
        rtcEngine()!!.setVideoSource(RtcVideoConsumer())
        joinChannel()
    }

    private fun initVideoModule() {
        mVideoManager = videoManager()
        mVideoManager!!.setCameraStateListener(object : VideoCaptureStateListener {
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

        mFURenderer = (mVideoManager!!.preprocessor as PreprocessorFaceUnity).fURenderer
        mBinding.fuView.setModuleManager(mFURenderer)
        mFURenderer?.setOnTrackStatusChangedListener { type, status ->
            runOnUiThread {
                mBinding.ivFaceDetect.setText(if (type == FURenderer.TRACK_TYPE_FACE) R.string.toast_not_detect_face else R.string.toast_not_detect_face_or_body)
                mBinding.ivFaceDetect.visibility = if (status > 0) View.INVISIBLE else View.VISIBLE
            }
        }
        mVideoManager!!.setPictureSize(
            AgoraConfig.CAPTURE_WIDTH,
            AgoraConfig.CAPTURE_HEIGHT
        )
        mVideoManager!!.setFrameRate(AgoraConfig.CAPTURE_FRAME_RATE)
        mVideoManager!!.setFacing(Constant.CAMERA_FACING_FRONT)
        mVideoManager!!.setLocalPreviewMirror(Constant.MIRROR_MODE_AUTO)
        mVideoManager!!.setLocalPreview(mBinding.localVideoView)
        // create screenshot to compare effect before and after using API
        /**
         * [io.agora.framework.PreprocessorFaceUnity.onPreProcessFrame]
         */
//        findViewById(R.id.btn_switch_camera).setOnLongClickListener { view ->
//            PreprocessorFaceUnity.needCapture = true
//            true
//        }
    }

    private fun joinChannel() {
        rtcEngine()!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
        rtcEngine()!!.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        rtcEngine()!!.enableLocalAudio(false)
        rtcEngine()!!.joinChannel(AgoraConfig.TOKEN, AgoraConfig.CHANNEL_NAME, null, 0)
    }

    override fun onStart() {
        super.onStart()
        val sensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        mVideoManager!!.startCapture()
        mFURenderer!!.queueEvent { mFURenderer!!.onSurfaceCreated() }
    }

    override fun finish() {
        mFinished = true
        val countDownLatch = CountDownLatch(1)
        mFURenderer!!.queueEvent {
            mFURenderer!!.onSurfaceDestroyed()
            countDownLatch.countDown()
        }
        try {
            countDownLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        mVideoManager!!.stopCapture()
        mVideoManager!!.setCameraStateListener(null)
        mFURenderer!!.setOnTrackStatusChangedListener(null)
        rtcEngine()!!.leaveChannel()
        super.finish()
    }

    override fun onStop() {
        super.onStop()
        if (!mFinished) {
            mVideoManager!!.stopCapture()
        }
        mSensorManager!!.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
//        Log.i(
//            "soohyangA",
//            "onJoinChannelSuccess " + channel + " " + (uid and 0xFFFFFFFFL)
//        )
    }

    override fun onUserOffline(uid: Int, reason: Int) {
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
        //TODO("Not yet implemented")
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        if (mRemoteUid == -1 && state == Constants.REMOTE_VIDEO_STATE_DECODING) {
            runOnUiThread {
                mRemoteUid = uid
                setRemoteVideoView(uid)
            }
        }
    }

    private fun setRemoteVideoView(uid: Int) {
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