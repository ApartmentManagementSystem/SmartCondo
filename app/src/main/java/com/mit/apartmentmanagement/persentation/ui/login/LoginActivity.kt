package com.mit.apartmentmanagement.persentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityLoginBinding
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.persentation.ui.MainActivity
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import com.mit.apartmentmanagement.presentation.viewmodels.LoginViewModel
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
        setupUI()
        observerLoginResult()
    }

    private fun observerLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    hideProcessBar()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showProcessBar()
                }

            }
        }

    }

    private fun setupUI() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (checkBox.isChecked) {
                    val email = edtEmail.text.toString().trim()
                    val password = edtPassword.text.toString().trim()
                    val (isValid, errorMessage) = validateCredentials(email, password)
                    if (!isValid) {
                        val loginRequest = LoginRequest(email, password)
                        loginViewModel.login(loginRequest)
                        showProcessBar()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.please_agree_to_our_terms_and_conditions),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun showProcessBar() {
        binding.spinKit.visibility = View.VISIBLE
        binding.dimOverlay.visibility = View.VISIBLE
    }

    private fun hideProcessBar() {
        binding.spinKit.visibility = View.GONE
        binding.dimOverlay.visibility = View.GONE
    }


    private fun validateCredentials(email: String, password: String): Pair<Boolean, String> {
        // 1. Không để trống
        if (email.isBlank()) {
            return false to "Email must not be empty"
        }
        if (password.isBlank()) {
            return false to "Password must not be empty"
        }

        // 2. Email phải đúng định dạng
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false to "Invalid email format"
        }

        // 3. Password tối thiểu 6 ký tự
        if (password.length < 6) {
            return false to "Password must be at least 6 characters"
        }

        // 4. Ngăn chặn ký tự nguy hiểm dễ dẫn đến SQL Injection
        //    (trong client chỉ mang tính cảnh báo, backend vẫn phải dùng parameterized query)
        val forbiddenPatterns = listOf(";", "--", "'", "\"", "/*", "*/", "DROP ", "SELECT ")
        forbiddenPatterns.forEach { pat ->
            if (email.contains(pat, ignoreCase = true) || password.contains(
                    pat,
                    ignoreCase = true
                )
            ) {
                return false to "Input contains invalid characters"
            }
        }

        // Nếu qua hết, hợp lệ
        return true to ""
    }

//    private fun initController() {
//        binding.btnSignIn.setOnClickListener {
//            val email = binding.txtEmail.text.toString().trim()
//            val password = binding.txtPassword.text.toString().trim()
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            authViewModel.login(LoginRequest(email,password))
//        }
//        authViewModel.loginResult.observe(this) { result ->
//            result.onSuccess { token ->
//                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
//                Log.d("LoginViewModel", "Đăng nhập thành công: $token")
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }.onFailure { error ->
//                Toast.makeText(this, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//        binding.txtForgotPassword.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
//                }
//                MotionEvent.ACTION_UP -> {
//                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction {
//                    startActivity(Intent(this, ForgetPasswordActivity::class.java))
//                    }.start()
//                }
//                MotionEvent.ACTION_CANCEL -> {
//                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
//                }
//            }
//            true // Trả về true để không bị override bởi onClick khác
//        }


}
