package com.threadtester.m.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.threadtester.m.app.R
import com.threadtester.m.app.databinding.FragmentArithmeticBinding
import kotlinx.coroutines.*
import java.lang.Runnable

const val MAX_TOTAL_TIME = 10
const val MAX_QUESTION_TIME = 3

class ArithmeticGameFragment : Fragment()
{
    /*****************************************************/
    var mBinding : FragmentArithmeticBinding? = null
    lateinit var mHandler : Handler
    /*****************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentArithmeticBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.btnStart.setOnClickListener(onClickListener)

        createHandler()
        createJob()
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }

    val onClickListener = View.OnClickListener {
        when(it.id)
        {
            R.id.btn_start->{
                runnableThreadStart()

                CoroutineScope(Dispatchers.Main).launch{
                    coroutineLazy()
                }
            }
        }
    }

    fun createHandler(){
        mHandler = Handler(Looper.getMainLooper()){_msg->
            //Log.i("bbbb", "${_msg.arg1}")
            mBinding?.let{
                it.totalProgress.progress = _msg.arg1
                //it.progress1.tint = _msg.arg1.toString()
            }
            true
        }
    }

    fun runnableThreadStart(){
        val timerThread2 = Thread(Runnable {
            for (i in 0..100){
                try{
                    Thread.sleep(100)
                    //Log.i("aaaa", "${i}")

                    val message = Message().apply {
                                    what = 1
                                    arg1 = i
                                }

                    mHandler.sendMessage(message)
                }
                catch (_e : InterruptedException){
                    Log.i("bbbb", "${_e.message}")
                }
            }
        })
        timerThread2.start()
    }

    fun coroutineStart(){

    }

    var mJob1 : Job? = null
    fun createJob(){
        mJob1 = CoroutineScope(Dispatchers.Main).launch(start = CoroutineStart.LAZY){
            Log.i("aaaa", "job1 LAZY 실행")
        }
    }

    suspend fun coroutineLazy(){
        mJob1?.join()
    }
}