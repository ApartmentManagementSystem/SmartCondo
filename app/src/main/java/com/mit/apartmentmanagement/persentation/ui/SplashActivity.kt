package com.mit.apartmentmanagement.persentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivitySplashBinding
import com.mit.apartmentmanagement.persentation.ui.login.LoginActivity
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import com.mit.apartmentmanagement.persentation.viewmodels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addObservers()
        splashViewModel.checkLoggedIn()

    }

    private fun addObservers() {
        splashViewModel.isLoggedIn.observe(this) { isLoggedIn ->
            when (isLoggedIn) {
                is NetworkResult.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is NetworkResult.Error -> {
                    if (isLoggedIn.message == "No network connection") {
                        showNoInternetDialog(
                            onRetry = {  splashViewModel.checkLoggedIn() })
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    private fun showNoInternetDialog(onRetry: () -> Unit = {}) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.no_internet_connection))
            .setMessage(getString(R.string.please_check_your_internet_connection_and_try_again))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.retry)) { dialog, _ ->
                dialog.dismiss()
                onRetry()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

}
