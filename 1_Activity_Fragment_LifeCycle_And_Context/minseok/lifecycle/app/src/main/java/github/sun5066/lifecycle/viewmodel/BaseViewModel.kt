package github.sun5066.lifecycle.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(private val app: Application) : AndroidViewModel(app) {

    protected val applicationContext: Context get() = app.applicationContext

}