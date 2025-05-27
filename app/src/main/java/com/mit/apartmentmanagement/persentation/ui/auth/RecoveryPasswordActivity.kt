package com.mit.apartmentmanagement.persentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.mit.apartmentmanagement.databinding.ActivityRecoveryPasswordBinding
import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.persentation.ui.BaseActivity
import com.mit.apartmentmanagement.persentation.ui.login.LoginActivity
import com.mit.apartmentmanagement.domain.util.NetworkResult
import com.mit.apartmentmanagement.persentation.viewmodels.RecoveryPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityRecoveryPasswordBinding
    private val recoveryPasswordViewModel: RecoveryPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//                val recoveryPasswordRequest = RecoveryPasswordRequest(resetCode,newPassword,confirmPassword)
//                recoveryPasswordViewModel.recoveryPassword(recoveryPasswordRequest)

                val (isValid, errorMessage) = validateCredentials(resetCode,newPassword,confirmPassword)
                    val recoveryPasswordRequest = RecoveryPasswordRequest(resetCode,newPassword,confirmPassword)
                    recoveryPasswordViewModel.recoveryPassword(recoveryPasswordRequest)
                    showProcessBar()
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
                    hideProcessBar()
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    Log.d("RecoveryPasswordViewModel", result.message ?: "Unknown error")
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