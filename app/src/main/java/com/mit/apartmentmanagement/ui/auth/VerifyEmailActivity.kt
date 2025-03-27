package com.mit.apartmentmanagement.ui.auth

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityVerifyEmailBinding

class VerifyEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyEmailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
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
        binding.btnConfirm.setOnClickListener{
            if (binding.otp1.text.toString().isEmpty() || binding.otp2.text.toString().isEmpty() || binding.otp3.text.toString().isEmpty() || binding.otp4.text.toString().isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Xác nhận thành công", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecoveryPasswordActivity::class.java))

            }
        }
        binding.txtSendBack.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction {
                        //code Here
                    }.start()
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            true // Trả về true để không bị override bởi onClick khác
        }

        // Xu ly chuc nang tu dong dien
        val otpFields = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4)

        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && index < otpFields.size - 1) {
                        otpFields[index + 1].requestFocus() // Chuyển sang ô tiếp theo
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.isEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus() // Chỉ quay lại ô trước nếu ô hiện tại rỗng
                    }
                }
                false
            }
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