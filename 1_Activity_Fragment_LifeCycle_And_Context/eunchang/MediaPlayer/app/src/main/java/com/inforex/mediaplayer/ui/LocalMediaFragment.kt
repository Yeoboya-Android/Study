package com.inforex.mediaplayer.ui

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.data.MediaInfoData
import com.inforex.mediaplayer.databinding.FragmentLocalMediaBinding
import com.inforex.mediaplayer.databinding.ItemMediaListBinding
import com.inforex.mediaplayer.ui.base.BaseFragment
import com.inforex.mediaplayer.ui.base.BaseNavigator
import com.inforex.mediaplayer.util.DataBindingViewHolder
import com.inforex.mediaplayer.util.ImageUtil

class LocalMediaFragment : BaseFragment<FragmentLocalMediaBinding, MainViewModel>() {

    override val mViewModel: MainViewModel by activityViewModels()

    override fun getLayoutResourceId() = R.layout.fragment_local_media

    override fun initDataBinding() {
        mBinding?.vm = mViewModel
        mBinding?.lifecycleOwner = viewLifecycleOwner
    }
    override fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
        queryLocalMediaFiles {
            setAdapter(it)
        }
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }


    private fun queryLocalMediaFiles(callback: (ArrayList<MediaInfoData>) -> Unit) {
        val contentResolver = requireContext().contentResolver
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

                val mediaInfoData = MediaInfoData(
                    thumbnailUri = thumbnail,
                    title = title,
                    artistName = artist,
                    duration = duration,
                    path = path
                )

                list.add(mediaInfoData)
            } while (it.moveToNext())
        }

        cursor?.close()
        callback.invoke(list)
    }

    private fun setAdapter(list: ArrayList<MediaInfoData>) {
        mBinding?.recyclerViewMediaList?.run {
            adapter = MediaListAdapter(list)
            layoutManager = LinearLayoutManager(requireContext())
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

            ImageUtil.setImage(requireContext(), binding.thumbnail, mediaInfo.thumbnailUri, defaultResId = R.drawable.example_thumbnail)
            binding.title.text = mediaInfo.title
            binding.artist.text = mediaInfo.artistName
            binding.containerItem.setOnClickListener {
                mViewModel.onClickMediaItem(mediaInfo)
            }
        }
    }


    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (activity as BaseNavigator).gotoLocalMediaListFragment(false)
        }
    }


}
