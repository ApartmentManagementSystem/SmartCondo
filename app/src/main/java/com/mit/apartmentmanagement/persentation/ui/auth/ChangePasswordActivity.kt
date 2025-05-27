package com.mit.apartmentmanagement.persentation.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.data.network.TokenManager
import com.mit.apartmentmanagement.databinding.ActivityChangePasswordBinding
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val tokenManager: TokenManager by lazy { TokenManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
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
//        binding.btnChangePassword.setOnClickListener {
//            val currentPassword = binding.txtCurrentPassword.text.toString().trim()
//            val newPassword = binding.txtNewPassword.text.toString().trim()
//            val confirmPassword = binding.txtConfirmPassword.text.toString().trim()
//
//            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
//                showToast("Vui lòng nhập đầy đủ thông tin")
//                return@setOnClickListener
//            }
//
//            if (newPassword != confirmPassword) {
//                showToast("Mật khẩu mới không khớp")
//                return@setOnClickListener
//            }
//            authViewModel.changePassword(ChangePasswordRequest(currentPassword,newPassword,confirmPassword))
//
//        }
//        authViewModel.changePasswordResult.observe(this) { result ->
//            result.onSuccess {
//                Toast.makeText(this, "Đổi mật khẩu mới thành công!", Toast.LENGTH_SHORT).show()
//                tokenManager.clearTokens()
//                val intent = Intent(this, LoginActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//                finishAffinity()
//            }.onFailure { error ->
//                Toast.makeText(this, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        }

    }
    private fun controlStatusBar() {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.mainColor)

        // Đặt icon status bar thành màu trắng vì background là màu tối
        window.decorView.systemUiVisibility = 0
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
}