package com.soo.screenrotate

import android.Manifest
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.soo.screenrotate.databinding.ActivityMainBinding
import com.soo.screenrotate.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModels()
    lateinit var mBinding: ActivityMainBinding

    @JvmField
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mBinding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = mViewModel
            clickListener = onClickListener
        }
    }


    private val onClickListener = View.OnClickListener {
        when(it.id) {
            R.id.btn_broadcaster_lan -> checkPermission {
                enterRoom(true, "landscape")
            }
            R.id.btn_broadcaster_pot -> checkPermission {
                enterRoom(true, "portrait")
            }
            R.id.btn_audience -> checkPermission {
                enterRoom(false, "landscape")
            }
            R.id.btn_rotate_fragment -> {
                startRotateTest()
            }
        }
    }

    private fun checkPermission(granted:()->Unit) {
        TedPermission.with(applicationContext)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    granted()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(applicationContext, "권한 획득 실패", Toast.LENGTH_SHORT).show()
                }
            })
            .setDeniedMessage("권한을 거부하면이 서비스를 사용할 수 없습니다.\\n[설정] > [권한]에서 권한을 설정하십시오.")
            .setPermissions(*PERMISSIONS)
            .check()
    }

    private fun enterRoom(isBroadcaster: Boolean, orientation: String) {
        val liveIntent = Intent(this, LiveActivity::class.java).apply {
            putExtra(LiveActivity.KEY_IS_BROADCASTER, isBroadcaster)
            putExtra(LiveActivity.KEY_ORIENTATION, orientation)
        }
        startActivity(liveIntent)
    }

    private fun startRotateTest() {
        startActivity(Intent(this, RotateTestActivity::class.java))
    }


}