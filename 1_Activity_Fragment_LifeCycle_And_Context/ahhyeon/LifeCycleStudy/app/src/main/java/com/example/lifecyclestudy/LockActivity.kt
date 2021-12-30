package com.example.lifecyclestudy

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.preference.PreferenceManager
import com.example.lifecyclestudy.databinding.ActivityLockBinding

class LockActivity : BaseActivity<ActivityLockBinding>() {

    override fun getLayoutResourceId(): Int = R.layout.activity_lock
    private val mViewModel : LockViewModel by viewModels()

    override fun initDataBinding() {
        mBinding.apply {
            lifecycleOwner = this@LockActivity
            viewmodel = mViewModel
            clicklistener = mClickListener
        }
    }

    override fun initView() {}

    companion object {
        fun startLockActivity(_context: Context) {
            val intent = Intent(_context, LockActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }
            _context.startActivity(intent)
        }
    }

    private val mClickListener = View.OnClickListener { v ->
        when(v?.id) {
            R.id.btnOk -> {
                if (mViewModel.getPassWord()) {
                    val intent = Intent(this@LockActivity, MainActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                    startActivity(intent)
                } else Toast.makeText(this, "비밀번호를 다시 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}