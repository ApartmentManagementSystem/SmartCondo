package com.mit.apartmentmanagement.persentation.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityProfileUserBinding

class ProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlStatusBar()
        initController()


    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initController() {
        binding.iconBack.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction { finish() }.start()
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            true // Trả về true để không bị override bởi onClick khác
        }
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun controlStatusBar() {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.mainColor)

        // Đặt icon status bar thành màu trắng vì background là màu tối
        window.decorView.systemUiVisibility = 0
    }
}