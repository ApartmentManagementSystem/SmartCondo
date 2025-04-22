package com.mit.apartmentmanagement.persentation.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityRecoveryPasswordBinding
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.persentation.ui.MainActivity
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import com.mit.apartmentmanagement.persentation.viewmodels.RecoveryPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.http.Header

@AndroidEntryPoint
class RecoveryPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryPasswordBinding
    private val recoveryPasswordViewModel: RecoveryPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecoveryPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        observerRecoveryPasswordResult()
    }
    private fun setupUI() {
        binding.apply {
            btnConfirm.setOnClickListener {

                val resetCode = edtResetCode.text.toString().trim()
                val newPassword = edtNewPassword.text.toString().trim()
                val confirmPassword = edtConfirmPassword.text.toString().trim()
                val (isValid, errorMessage) = validateCredentials(resetCode,newPassword,confirmPassword)
                if (!isValid) {
                    val recoveryPasswordRequest = RecoveryPasswordRequest(resetCode,newPassword,confirmPassword)
                    recoveryPasswordViewModel.recoveryPassword(recoveryPasswordRequest)
                    showProcessBar()
                } else {
                    Toast.makeText(
                        this@RecoveryPasswordActivity,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

    }
    private fun observerRecoveryPasswordResult() {
        recoveryPasswordViewModel.recoveryPasswordResult.observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    hideProcessBar()
                    startActivity(Intent(this, LoginActivity::class.java))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
    private fun showProcessBar() {
        binding.spinKit.visibility = View.VISIBLE
        binding.dimOverlay.visibility = View.VISIBLE
    }

    private fun hideProcessBar() {
        binding.spinKit.visibility = View.GONE
        binding.dimOverlay.visibility = View.GONE
    }
    private fun validateCredentials(resetCode: String, newPassword: String, confirmPassword: String): Pair<Boolean, String> {
        // 1. Không để trống
        if (resetCode.isBlank()) {
            return false to "Reset code must not be empty"
        }
        if (newPassword.isBlank()) {
            return false to "New password must not be empty"
        }
        if (confirmPassword.isBlank()) {
            return false to "Confirm password must not be empty"
        }
        // 3. Password tối thiểu 6 ký tự
        if (newPassword.length < 6) {
            return false to "Password must be at least 6 characters"
        }
        if (newPassword!=confirmPassword) {
          return false to "Password not match"
        }

        return true to ""
    }
}