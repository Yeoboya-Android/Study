package com.threadtester.m.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.threadtester.m.app.databinding.ActivityMainBinding
import com.threadtester.m.app.thread.TimerThread
import com.threadtester.m.app.ui.fragment.ArithmeticGameFragment
import com.threadtester.m.app.util.ActivityUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.lang.AssertionError
import java.lang.IllegalArgumentException
import java.lang.Runnable

class MainActivity : AppCompatActivity() {
    lateinit var mBinding : ActivityMainBinding
    lateinit var mHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        addArithmeticFragment()

        //createHandler()
        //testThread()
        //testCoroutine()
        //testCoroutine2()
        //testCoroutine3()
        testCoroutine4()
        //testCoroutine5()
        //testCoroutine6()
        //testCoroutine7()

        //sampleCoroutine()

        //testJobCancel()
        //testExceptionHaneler()
        //testCustomThread()
    }

    fun addArithmeticFragment(){
        val fragmentManager = supportFragmentManager
        ActivityUtil.addFragmentToActivity(fragmentManager, ArithmeticGameFragment(), R.id.content)
    }

    fun createHandler(){
        mHandler = Handler(Looper.getMainLooper()){
            Log.i("bbbb", "${it.arg1}")
            mBinding.timer.text = it.arg1.toString()
            true
        }
    }

    fun testThread(){
        val timerThread = TimerThread()
        timerThread.start()

        val timerThread2 = Thread(Runnable {
            for (i in 0..10){
                try{
                    Thread.sleep(1000)
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

    fun sampleCoroutine(){
        val job1 = CoroutineScope(Dispatchers.Default).launch{
            for(i in 1..10000){
                Log.i("aaaa", "job1 $i")
            }
        }

        val job2 = CoroutineScope(Dispatchers.IO).launch{
            for(i in 1..10000){
                Log.i("aaaa", "job2 $i")
            }
        }
    }

    fun testCoroutine(){
        val job1 = CoroutineScope(Dispatchers.Main).launch {
            Log.i("aaaa", "job1 수행시작")

            val job3Result = suspendCoroutine()

            Log.i("aaaa", "job1 await 수행시작")
            job3Result.forEach {
                Log.i("aaaa", "${it}")
            }
        }

        Log.i("aaaa", "job2 수행======")
        val job2 = CoroutineScope(Dispatchers.Main).launch{
            Log.i("aaaa", "job2 수행")
        }

        /*CoroutineScope(Dispatchers.IO).launch {
            val deferredInt : Deferred<Int> = async {
                Log.i("aaaa", "xxxxxxx")

                Thread.sleep(10000)
                1
            }

            val value = deferredInt.await()
            mBinding.timer.text = value.toString()
            Log.i("aaaa", "${value}")
        }*/
    }

    suspend fun suspendCoroutine() : List<Int>{
        val job3 = CoroutineScope(Dispatchers.IO).async {
            Log.i("aaaa", "job3 수행시작")
            //(1..10000000).sortedByDescending { it }
            delay(10000)
            (1..1000).sortedByDescending { it }
        }

        return job3.await()
    }

    fun testJobCancel(){
        val job = CoroutineScope(Dispatchers.Main).launch{
            Log.i("aaaa", "job2 수행")
            delay(1000)
        }

        job.invokeOnCompletion { _throwable->
            when(_throwable){
                is CancellationException -> Log.i("aaaa", "$_throwable")
                null -> Log.i("aaaa", "success")
            }
        }

        job.cancel("cancel job", InterruptedException("Cancelled!!!"))
    }

    fun testExceptionHaneler(){
        CoroutineScope(Dispatchers.IO).launch{
            val supervisor = SupervisorJob()

            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                Log.i("aaaa", "$exception")
            }

            val job1 = CoroutineScope(Dispatchers.IO+supervisor).async { // root coroutine, running in GlobalScope
                throw IllegalArgumentException()
                0
            }

            val job2 = CoroutineScope(Dispatchers.IO+supervisor).async { // root coroutine, running in GlobalScope
                //throw InterruptedException()
                Log.i("aaaa", "둘째 잡은 살아있다.")
                0
            }

            CoroutineScope(exceptionHandler).launch{
                val aa = job1.join()
                val bb = job2.join()
                Log.i("aaaa", "여기까지 실행된다.")
            }
        }
    }

    fun testCustomThread(){
        val singleThreadDispatcher = newSingleThreadContext("Single Thread ThreadPool")
        val dispatcherWith2Threads = newFixedThreadPoolContext(2,"ThreadPool with 2 threads")

        CoroutineScope(singleThreadDispatcher).launch {
            val aaaa = CoroutineScope(dispatcherWith2Threads).async {
                (1..100).sortedByDescending { it }
            }

            val bbbb = CoroutineScope(dispatcherWith2Threads).async {
                (1..100).sortedByDescending { it }
            }

            val cccc = CoroutineScope(dispatcherWith2Threads).async {
                (1..100).sortedByDescending { it }
            }

            val result1 = aaaa.await()
            val result2 = bbbb.await()
            val result3 = cccc.await()

            Log.i("aaaa", "$result1")
            Log.i("aaaa", "$result2")
            Log.i("aaaa", "$result3")
        }
    }



    fun testCoroutine2(){
        val exceptionHandler = CoroutineExceptionHandler{_, _exception->
            when(_exception)
            {
                is IllegalArgumentException -> Log.i("aaaa", "More Argument Needed To Process Job")
                is InterruptedException -> Log.i("aaaa", "Job Interrupted")
            }
        }

        val aaa =  Dispatchers.IO + exceptionHandler
        val getExceptionHandler = aaa[exceptionHandler.key]
        val getExceptionHandler2 = aaa[Dispatchers.IO.key]

        if(getExceptionHandler!!.key == exceptionHandler.key){
            Log.i("aaaa", "same")
        }

        if(getExceptionHandler2!!.key == exceptionHandler.key){
            Log.i("aaaa", "same")
        }

        val minusKey = aaa.minusKey(exceptionHandler.key)
        if(minusKey == exceptionHandler.key){
            Log.i("aaaa", "same")
        }

        if(minusKey == Dispatchers.IO){
            Log.i("aaaa", "same")
        }



        CoroutineScope(aaa).launch{
            val deferred : Deferred<String> = CoroutineScope(Dispatchers.IO).async {
                throw IllegalArgumentException()

                //Thread.sleep(10000)
                "Deferred Result"
            }

            deferred.await()
        }
    }

    fun testCoroutine3(){
        CoroutineScope(Dispatchers.IO).launch{
            val result : String = withContext(Dispatchers.IO){
                Thread.sleep(10000)
                "Async Result"
            }

            Log.i("aaaa", "$result")
        }
    }

    fun testCoroutine4(){
        CoroutineScope(Dispatchers.IO).launch{
            val supervisor = SupervisorJob()

            val exceptionHandler = CoroutineExceptionHandler{_, _exception->
                when(_exception)
                {
                    is IllegalArgumentException -> Log.i("aaaa", "More Argument Needed To Process Job")
                    is InterruptedException -> Log.i("aaaa", "Job Interrupted")
                }
            }

            supervisorScope {
                val firstChildJob = launch {
                    Log.d("aaaa", "firstJob")
                    throw AssertionError("첫째 AssertionError로 인해 취소됩니다.")
                }

                val secondChildJob = launch(Dispatchers.Default) {
                    delay(1000)
                    Log.d("aaaa", "secondJob")
                    throw AssertionError("둘째 AssertionError로 인해 취소됩니다.")

                    Log.d("aaaa", "secondJob")
                }

                firstChildJob.join()
                secondChildJob.join()
            }
        }
    }

    fun testCoroutine5(){
        val aaa = GlobalScope.launch() {
            repeat(10) {
                delay(1000L)
                Log.d("aaaa", "I'm working.")
            }

            val firstChildJob1 = async(Dispatchers.IO){
                var total = 0
                for(i in 1..10){
                    total += i
                    delay(100)
                }
                Log.d("aaaa", "firstChildJob1 : ${total}")

                total
            }

            val firstChildJob2 = async(Dispatchers.IO){
                var total = 0
                for(i in 1..10){
                    total += i
                    delay(100)
                }
                Log.d("aaaa", "firstChildJob2 : ${total}")

                total
            }

            val aaa =firstChildJob2.await()
            Log.d("aaaa", "11111")
            val bbb = firstChildJob1.await()
            Log.d("aaaa", "22222")


            Log.d("aaaa", "${aaa}, ${bbb}")
        }

        runBlocking {
            //delay(3000L)


            aaa.join()
            Log.d("aaaa", "delay 3000")

        }
    }

    fun testCoroutine6(){
        runBlocking {
            //delay(100000L)
            //Log.d("aaaa", "delay 100000000")
            val numbers = productNumbers()
            val squares = squares(numbers)

            var x = 0
            while (true)
            {
                Log.i("aaaa", "${squares.receive()}")
                x++
                if(5 < x) {
                    coroutineContext.cancelChildren()
                    break
                };
            }


        }
    }

    fun CoroutineScope.productNumbers() = produce<Int>{
        var x = 1
        while(true){
            send(x++)
            delay(100L)
        }
    }

    fun CoroutineScope.squares(numbers : ReceiveChannel<Int>) : ReceiveChannel<Int>{
        return produce {
            for (x in numbers){
                send(x * x)
            }
        }
    }

    fun testCoroutine7(){
        runBlocking {
            val producer = productNumbers()

            repeat(5){
                launchProcessor(it, producer)
            }

            delay(1000L)
            producer.cancel()
        }
    }

    fun CoroutineScope.launchProcessor(id : Int, channel : ReceiveChannel<Int>){
        launch {
            for(msg in channel){
                Log.i("aaaa", "processor $id received $msg" )
            }
        }
    }
}