package com.example.studytestapp

import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.studytestapp.chatlist.ChatListFragment
import com.example.studytestapp.databinding.ActivityMainBinding
import com.example.studytestapp.home.HomeFragment
import com.example.studytestapp.mypage.MyPageFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutResourceId() = R.layout.activity_main
    private val viewModel : MainViewModel by viewModels()

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val homeFragment = HomeFragment()
    private val chatFragment = ChatListFragment()
    private val myPageFragment = MyPageFragment()

    override fun initDataBinding() {
        mBinding.lifecycleOwner = this
        mBinding.vm = viewModel
    }

    override fun initView() {
        replaceFragment(homeFragment)

        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> goToChatFragemnt()

                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }
    }

    private fun goToChatFragemnt() {
        if (auth.currentUser != null) {
            replaceFragment(chatFragment)
        } else {
            Toast.makeText(applicationContext, "로그인 후 사용해주세요!.", Toast.LENGTH_LONG).show()
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                addToBackStack(null)
                commit()
            }
    }
}

