package com.mit.apartmentmanagement.persentation.ui.verify_email

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityVerifyEmailBinding
import com.mit.apartmentmanagement.persentation.ui.BaseActivity
import com.mit.apartmentmanagement.persentation.ui.auth.RecoveryPasswordActivity
import com.mit.apartmentmanagement.domain.util.NetworkResult
import com.mit.apartmentmanagement.persentation.viewmodels.VerifyEmailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailActivity : BaseActivity() {
    private lateinit var binding: ActivityVerifyEmailBinding
    private val verifyEmailViewModel: VerifyEmailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        observerIsRegistered()
    }

    private fun observerIsRegistered() {
        verifyEmailViewModel.isRegistered.observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    hideProcessBar()
                    startActivity(Intent(this, RecoveryPasswordActivity::class.java))
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

    private fun setupUI() {
        binding.btnSendCode.setOnClickListener {
            val isEmailValid = validateEmail(binding.textInputLayout, binding.edtEmail)
            if (isEmailValid) {
                verifyEmailViewModel.checkEmailRegistered(binding.edtEmail.text.toString().trim())
            }
        }
    }

    fun validateEmail(
        emailInputLayout: TextInputLayout,
        emailEditText: TextInputEditText
    ): Boolean {
        val email = emailEditText.text.toString().trim()
        return if (email.isEmpty()) {
            emailInputLayout.error = getString(R.string.please_enter_your_email)
            Toast.makeText(this, getString(R.string.please_enter_your_email), Toast.LENGTH_SHORT)
                .show()
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = getString(R.string.invalid_email_format)
            Toast.makeText(this, getString(R.string.invalid_email_format), Toast.LENGTH_SHORT)
                .show()
            false
        } else {
            emailInputLayout.error = null
            true
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


}