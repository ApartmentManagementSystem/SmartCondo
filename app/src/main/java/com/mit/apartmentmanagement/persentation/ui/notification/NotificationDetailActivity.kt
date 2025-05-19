package com.mit.apartmentmanagement.persentation.ui.notification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mit.apartmentmanagement.databinding.ActivityNotificationDetailBinding
import com.mit.apartmentmanagement.domain.model.Notification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    }

    private fun handleIntent() {
        val notification = intent.getParcelableExtra<Notification>("notification")
        notification?.let {
            binding.apply {
                toolbar.title = it.title
                notificationTitle.text = it.title
                notificationContent.text = it.content
                notificationTime.text = it.createdAt
                notificationTopic.text = it.topic
            }
            viewModel.markNotificationAsRead(it.notificationId)
        }
    }
}