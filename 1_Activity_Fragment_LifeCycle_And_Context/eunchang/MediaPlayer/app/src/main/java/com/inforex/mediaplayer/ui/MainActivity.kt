package com.inforex.mediaplayer.ui

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.data.MediaInfoData
import com.inforex.mediaplayer.databinding.ItemMediaListBinding


class MainActivity : AppCompatActivity() {

    private var mMediaPlayer: MediaPlayer? = null

    private var playView: FrameLayout? = null
    private var playThumbnail: ImageView? = null
    private var playTitle: TextView? = null
    private var playArtist: TextView? = null
    private var playCloseBtn: ImageButton? = null

    private var mLifecycleObserver = MediaPlayerLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(mLifecycleObserver)

        setView()
        requestPermission()
    }

    private inner class MediaPlayerLifecycleObserver : LifecycleObserver {

        @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
        private fun onResume() {
            Log.d("qwe123", "MediaPlayerLifecycleObserver.onResume():::")
            mMediaPlayer?.start()
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
        private fun onPause() {
            Log.d("qwe123", "MediaPlayerLifecycleObserver.onPause():::")
            mMediaPlayer?.pause()
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
        private fun onDestroy() {
            Log.d("qwe123", "MediaPlayerLifecycleObserver.onDestroy():::")
            releaseMediaPlayer()
        }
    }


    /** todo
     * Fragment 생성
     * MVVM 작업
     * Api로 playList 받아오기
     * Service 작업
     * Coroutine 작업
     * */
    private fun setView() {
        playView = findViewById(R.id.play_view)
        playThumbnail = findViewById(R.id.play_thumbnail)
        playTitle = findViewById(R.id.play_title)
        playArtist = findViewById(R.id.play_artist)
        playCloseBtn = findViewById(R.id.btn_close)
        playCloseBtn?.setOnClickListener {
            releaseMediaPlayer()
            setFloatingView(null)
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) queryLocalMediaFiles { setAdapter(it) }
        else Log.i("qwe123", "PERMISSION_DENIED")
    }

    private fun onClickListItem(mediaInfo: MediaInfoData) {
        setFloatingView(mediaInfo)
        playMedia(mediaInfo)
    }

    private fun playMedia(mediaInfo: MediaInfoData) {
        mMediaPlayer?.stop()
        mMediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA) .build()
            )
            setDataSource(applicationContext, mediaInfo.path.toUri())
            prepare()
            start()
        }
    }

    private fun setFloatingView(mediaInfo: MediaInfoData?) {
        mediaInfo?.let {
            playView?.visibility = View.VISIBLE
            playThumbnail?.setImageURI(it.thumbnailUri)
            playTitle?.text = it.title
            playArtist?.text = it.artistName
        } ?: run {
            playView?.visibility = View.GONE
        }
    }

    private fun releaseMediaPlayer() {
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    private fun queryLocalMediaFiles(callback: (ArrayList<MediaInfoData>) -> Unit) {
        val contentResolver = contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        val list = arrayListOf<MediaInfoData>()
        cursor?.takeIf { it.moveToFirst() }?.let {
            do {
                val albumId = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val albumArtUri = Uri.parse("content://media/external/audio/albumart")
                val thumbnail = ContentUris.withAppendedId(albumArtUri, albumId)
                val title = it.getString(it.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val duration = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val path = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))

                list.add(MediaInfoData(thumbnail, title, artist, duration, path))
            } while (it.moveToNext())
        }

        cursor?.close()
        callback.invoke(list)
    }

    private fun setAdapter(list: ArrayList<MediaInfoData>) {
        findViewById<RecyclerView>(R.id.recycler_view_media_list).apply {
            adapter = MediaListAdapter(list)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private inner class MediaListAdapter(private var mediaList: ArrayList<MediaInfoData>) : RecyclerView.Adapter<DataBindingViewHolder<ItemMediaListBinding>>() {

        override fun getItemCount() = mediaList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ItemMediaListBinding> {
            return DataBindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_media_list, parent, false))
        }

        override fun onBindViewHolder(holder: DataBindingViewHolder<ItemMediaListBinding>, position: Int) {
            val binding = holder.binding as ItemMediaListBinding

            val mediaInfo = mediaList[position]

            binding.thumbnail.setImageURI(mediaInfo.thumbnailUri)
            binding.title.text = mediaInfo.title
            binding.artist.text = mediaInfo.artistName
            binding.containerItem.setOnClickListener {
                onClickListItem(mediaInfo)
            }
        }
    }

    private inner class DataBindingViewHolder<T : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: T? = DataBindingUtil.bind(itemView)
    }
}