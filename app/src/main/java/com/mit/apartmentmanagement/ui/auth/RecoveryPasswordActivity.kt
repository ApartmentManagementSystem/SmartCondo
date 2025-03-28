package com.mit.apartmentmanagement.ui.auth

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
import com.mit.apartmentmanagement.databinding.ActivityRecoveryPasswordBinding
import com.mit.apartmentmanagement.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.http.Header

@AndroidEntryPoint
class RecoveryPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryPasswordBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityRecoveryPasswordBinding.inflate(layoutInflater)
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
        binding.btnConfirm.setOnClickListener {
            val code = binding.txtCode.text.toString().trim()
            val newPassword = binding.txtNewPassword.text.toString().trim()
            val confirmPassword = binding.txtConfirmPassword.text.toString().trim()

            if (code.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Vui lòng nhập đầy đủ thông tin")
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                showToast("Mật khẩu mới không khớp")
                return@setOnClickListener
            }
            authViewModel.recoveryPassword(code, newPassword, confirmPassword)


        }
        authViewModel.recoveryPasswordResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()

            }.onFailure { error ->
                Toast.makeText(this, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }



    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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