package com.inforex.mediaplayer.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.data.MediaInfoData
import com.inforex.mediaplayer.ui.base.BaseNavigator
import com.inforex.mediaplayer.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// todo MediaPlayer를 관리하는 별도의 클래스를 만들기
class MainViewModel : BaseViewModel() {

    private lateinit var mContext: Context
    private lateinit var mNavigator: BaseNavigator

    val defaultImageResId = R.drawable.example_thumbnail

    private val _isButtonEnable = MutableLiveData(false)
    private val _curThumbnailImagePath = MutableLiveData("")
    private val _curTitle = MutableLiveData("")
    private val _curArtist = MutableLiveData("")
    val isButtonEnable: LiveData<Boolean> get() = _isButtonEnable
    val curThumbnailImagePath: LiveData<String> get() = _curThumbnailImagePath
    val curTitle: LiveData<String> get() = _curTitle
    val curArtist: LiveData<String> get() = _curArtist

    private var mCurrentMedia: MediaInfoData? = null
    private var mMediaPlayer: MediaPlayer? = null

    fun init(applicationContext: Context, navigator: BaseNavigator) {
        mContext = applicationContext
        mNavigator = navigator
        coroutineTest()
    }

    val clickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_local_media -> mNavigator.gotoLocalMediaListFragment(true)
            R.id.btn_tape_media -> mNavigator.gotoTapeMediaListFragment(true)
            R.id.btn_close -> stopMediaPlayer()
        }
    }

    fun setButtonEnable(isEnable: Boolean) {
        _isButtonEnable.value = isEnable
    }

    fun onClickMediaItem(mediaInfo: MediaInfoData) {
        playMedia(mediaInfo)
        setCurrentMediaData(mediaInfo)
        mNavigator.showFloatingFragment(true)
    }

    private fun playMedia(mediaInfo: MediaInfoData) {
        mMediaPlayer?.stop()
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA) .build()
            )
            setDataSource(mContext, mediaInfo.path.toUri())
            prepare()
            start()
        }
    }

    private fun setCurrentMediaData(mediaInfo: MediaInfoData?) {
        mCurrentMedia = mediaInfo
        mCurrentMedia?.let {
            _curThumbnailImagePath.value = it.thumbnailUri.path
            _curTitle.value = it.title
            _curArtist.value = it.artistName
        }
    }

    private fun stopMediaPlayer() {
        mNavigator.showFloatingFragment(false)
        setCurrentMediaData(null)
        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }


    /** 코루틴 테스트 */
    private fun coroutineTest() {
        Log.d("qwe123", "coroutineTest() start")
        val myJob = viewModelScope.async(Dispatchers.Default) {
            test1()
            test2()
        }

        myJob.cancel()
        Log.d("qwe123", "coroutineTest() end")
    }

    private suspend fun test1() {
        delay(2000)
        Log.i("qwe123", "test1()")
    }

    private suspend fun test2() {
        delay(2000)
        Log.i("qwe123", "test2()")
    }
/*
    private fun test1() {
        Thread.sleep(2000)
        Log.i("qwe123", "test1()")
    }

    private fun test2() {
        Thread.sleep(2000)
        Log.i("qwe123", "test2()")
    }*/
}
