package com.mit.apartmentmanagement.persentation.ui.notification

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityNotificationDetailBinding
import com.mit.apartmentmanagement.domain.model.Notification
import com.mit.apartmentmanagement.persentation.ui.BaseActivity
import com.mit.apartmentmanagement.persentation.viewmodels.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
@AndroidEntryPoint
class NotificationDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityNotificationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        handleIntent()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toolbar.title = "Notification Detail"
    }

    private fun handleIntent() {
        val notification = intent.getParcelableExtra<Notification>("notification")
        notification?.let {
            binding.apply {
                notificationTitle.text = it.title
                notificationContent.text = it.content
                notificationTime.text = formatIsoToDate(it.createdAt)
            }
            Log.d("NotificationDetailActivity", notification.content)
        }
    }

    private fun formatIsoToDate(input: String): String {
        val odt = OffsetDateTime.parse(input)
        val formatter = DateTimeFormatter.ofPattern("mm:hh dd/MM/yyyy")
        return odt.format(formatter)
    }
}