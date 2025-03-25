package com.mit.apartmentmanagement.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityProfileUserBinding
import com.mit.apartmentmanagement.databinding.ActivityRecoveryPasswordBinding

class ProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlStatusBar()
        initController()


    }
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
            true // Trả về true để không bị override bởi onClick khác
        }
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
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