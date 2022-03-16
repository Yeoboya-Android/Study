package github.sun5066.lifecycle

import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TestService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("123123", "onStartCommand()")
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d("123123", "onBind()")
        return LocalBinder()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("123123", "onCreate()")
    }

    override fun onRebind(intent: Intent?) {
        Log.d("123123", "onRebind()")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("123123", "onUnbind()")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("123123", "onDestroy()")
    }

    fun count() {
        flow {
            (0..10).forEach { emit(it) }
        }.onEach {
            Log.d("123123", "count: $it")
        }.launchIn(lifecycleScope)
    }

    inner class LocalBinder : Binder() {
        val service: TestService get() = this@TestService
    }
}