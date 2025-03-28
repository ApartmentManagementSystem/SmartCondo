package com.mit.apartmentmanagement.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityForgetPasswordBinding
import com.mit.apartmentmanagement.ui.MainActivity
import com.mit.apartmentmanagement.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
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
            true
        }
        binding.txtSignIn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }.start()
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            true
        }

        binding.btnSendEmail.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.forgotPassword(email)
        }

        authViewModel.forgetPasswordResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Đã gửi email thành công!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecoveryPasswordActivity::class.java))
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun controlStatusBar() {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_real)

        // Kiểm tra chế độ Dark Mode
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Nếu đang ở Dark Mode -> Chữ status bar là trắng
            window.decorView.systemUiVisibility = 0
        } else {
            // Nếu đang ở Light Mode -> Chữ status bar là đen
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}