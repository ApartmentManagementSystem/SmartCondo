package com.mit.apartmentmanagement.persentation.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.mit.apartmentmanagement.persentation.ui.verify_email.VerifyEmailActivity
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
                    hideProcessBar()
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showProcessBar()
                }

            }
        }

    }

    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun setupUI() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (!checkBox.isChecked) {
                    toast(getString(R.string.please_agree_to_our_terms_and_conditions))
                    return@setOnClickListener
                }
                val email = edtEmail.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                validateCredentials(email, password)?.let { errorMsg ->
                    Log.d("LoginActivity", errorMsg)
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                loginViewModel.login(LoginRequest(email, password))
            }
            tvForgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, VerifyEmailActivity::class.java))
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


    private fun validateCredentials(email: String, password: String): String? {
        if (email.isBlank()) return getString(R.string.email_must_not_be_empty)
        if (password.isBlank()) return getString(R.string.password_must_not_be_empty)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return getString(R.string.invalid_email_format)
        if (password.length < 6) return getString(R.string.password_must_be_at_least_6_characters)

        val forbidden = listOf(";", "--", "'", "\"", "/*", "*/", "DROP ", "SELECT ")
        if (forbidden.any { it in email || it in password })
            return getString(R.string.input_contains_invalid_characters)

        return null
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
