package com.mit.apartmentmanagement.persentation.ui

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
//    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySplashBinding.inflate(layoutInflater)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000)

        setContentView(binding.root)

    }

    private fun checkLoginStatus() {
//        mainViewModel.checkLogin()
//        mainViewModel.isLogined.observe(this){ isLogined->
//            if(isLogined){
//                Toast.makeText(this,"Logined", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this,MainActivity::class.java))
//                finish()
//            }else{
//                Toast.makeText(this,"Not Logined", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
//        }
    }
}