package com.mit.apartmentmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityLoginBinding
import com.mit.apartmentmanagement.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initController()




    }
    private fun initController() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, ProfileUserActivity::class.java))
            loginViewModel.login(email, password)
        }
        binding.txtForgotPassword.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction {
                    startActivity(Intent(this, ForgetPasswordActivity::class.java))
                    }.start()
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            true // Trả về true để không bị override bởi onClick khác
        }
        loginViewModel.loginResult.observe(this) { result ->
            result.onSuccess { token ->
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                Log.d("LoginViewModel", "Đăng nhập thành công: $token")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
