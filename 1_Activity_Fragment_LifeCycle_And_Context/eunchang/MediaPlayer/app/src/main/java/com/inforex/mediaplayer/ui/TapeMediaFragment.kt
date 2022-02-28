package com.inforex.mediaplayer.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inforex.mediaplayer.R
import com.inforex.mediaplayer.data.BaseData
import com.inforex.mediaplayer.data.DataList
import com.inforex.mediaplayer.data.MediaInfoData
import com.inforex.mediaplayer.databinding.FragmentTapeMediaBinding
import com.inforex.mediaplayer.databinding.ItemMediaListBinding
import com.inforex.mediaplayer.ui.base.BaseFragment
import com.inforex.mediaplayer.ui.base.BaseNavigator
import com.inforex.mediaplayer.ui.base.MediaApiInterface
import com.inforex.mediaplayer.util.DataBindingViewHolder
import com.inforex.mediaplayer.util.ImageUtil
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TapeMediaFragment : BaseFragment<FragmentTapeMediaBinding, MainViewModel>() {

    override val mViewModel: MainViewModel by activityViewModels()

    override fun getLayoutResourceId() = R.layout.fragment_tape_media

    override fun initDataBinding() {
        mBinding?.vm = mViewModel
        mBinding?.lifecycleOwner = viewLifecycleOwner
    }


    override fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
        setApiInterface()
        getList()
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }

    private fun getList() = lifecycleScope.launch(Dispatchers.IO) {
        mMediaApiInterface?.let {
            requestAsync({ it.getTapeList(getRequestBody()) }).collect {
                if (it.code == "00000") requireActivity().runOnUiThread { setAdapter(it.data) }
            }
        }
    }

    private fun setAdapter(list: ArrayList<MediaInfoData>) {
        mBinding?.recyclerViewMediaList?.run {
            adapter = MediaListAdapter(list)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private val baseUrl = "https://liveapi.club5678.com"
    private var mMediaApiInterface: MediaApiInterface? = null
    private fun setApiInterface() {
        val client = OkHttpClient.Builder()
            .connectTimeout(2L, TimeUnit.MINUTES)
            .writeTimeout(2L, TimeUnit.MINUTES)
            .readTimeout(2L, TimeUnit.MINUTES)
            .build()

        mMediaApiInterface = retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MediaApiInterface::class.java)
    }

    private fun getRequestBody(): RequestBody {
        val filterJson = JSONObject().apply {
            put("searchSlct", "a")
            put("tapeClssCode", "")
            put("memNo", 0)
            put("memSex", "")
            put("updDate", "2022-1-13 17:29:15")
            put("pageNo", 1)
            put("pagePerCnt", 100)
        }

        val bodyJson = JSONObject().apply {
            put("tapeNo", 14301)
            put("tapeHostNo", 22120032)
            put("pageNo", 1)
            put("pagePerCnt", 100)
            put("type", "search")
            put("loginMemNo", 22016928)
            put("filter", filterJson)
            put("randomPlayList", JSONArray())
        }

        return RequestBody.create(MediaType.parse("application/json"), bodyJson.toString())
    }

    private suspend fun requestAsync(requestApi: suspend () -> BaseData<DataList<MediaInfoData>>, onError: ((Throwable) -> Unit)? = null) = flow {
        emit(requestApi())
    }
        .flowOn(Dispatchers.IO)
        .catch { error ->
            onError?.invoke(error) ?: error.printStackTrace()
        }

    private inner class MediaListAdapter(private var mediaList: ArrayList<MediaInfoData>) : RecyclerView.Adapter<DataBindingViewHolder<ItemMediaListBinding>>() {

        override fun getItemCount() = mediaList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ItemMediaListBinding> {
            return DataBindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_media_list, parent, false))
        }

        override fun onBindViewHolder(holder: DataBindingViewHolder<ItemMediaListBinding>, position: Int) {
            val binding = holder.binding as ItemMediaListBinding

            val mediaInfo = mediaList[position]

            ImageUtil.setImage(requireContext(), binding.thumbnail, mediaInfo.tapeImagePath)
            binding.title.text = mediaInfo.title
            binding.artist.text = mediaInfo.artistName
            binding.containerItem.setOnClickListener {
                mViewModel.onClickMediaItem(mediaInfo)
            }
        }
    }


    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (activity as BaseNavigator).gotoTapeMediaListFragment(false)
        }
    }

}
