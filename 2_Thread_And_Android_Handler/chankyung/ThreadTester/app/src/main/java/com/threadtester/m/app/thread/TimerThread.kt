package com.threadtester.m.app.thread

import android.util.Log

class TimerThread : Thread
{
    constructor() : super()
    constructor(_runnable : Runnable): super(_runnable)

    override fun run() {
        for (i in 0..10){
            try{
                Thread.sleep(1000)
                Log.i("aaaa", "${i}")
            }
            catch (_e : InterruptedException){
                Log.i("aaaa", "${_e.message}")
            }
        }
    }
}