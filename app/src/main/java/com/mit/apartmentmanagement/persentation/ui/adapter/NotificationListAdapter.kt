package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ItemNotificationListBinding
import com.mit.apartmentmanagement.domain.model.Notification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationListAdapter(
    private val onNotificationClicked: (Notification) -> Unit,
    private val onMarkAsReadClicked: (Notification) -> Unit = {},
    private val onViewDetailsClicked: (Notification) -> Unit = {}
) : PagingDataAdapter<Notification, NotificationListAdapter.NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position)?.let { notification ->
            holder.bind(notification)
        }
    }

    inner class NotificationViewHolder(
        private val binding: ItemNotificationListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { notification ->
                        onNotificationClicked(notification)
                    }
                }
            }

            binding.btnMarkAsRead.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { notification ->
                        onMarkAsReadClicked(notification)
                    }
                }
            }

            binding.btnViewDetails.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { notification ->
                        onViewDetailsClicked(notification)
                    }
                }
            }
        }

        fun bind(notification: Notification) {
            binding.apply {
                notificationTitle.text = notification.title
                notificationContent.text = notification.content
                notificationTime.text = getFormattedTime(notification.createdAt)
                // Set read status icon
                notificationStatusIcon.isVisible = notification.isRead
                // Update button text based on read status
                if (!notification.isRead) {
                    btnMarkAsRead.text = "Mark as Read"
                } else {
                    btnMarkAsRead.text = "Mark as Unread"
                }
            }
        }

        private fun getFormattedTime(createdAt: String): String {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = dateFormat.parse(createdAt) ?: return createdAt
                val now = Date()
                val diffInMillis = now.time - date.time

                return when {
                    diffInMillis < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                    diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                        "${minutes}m ago"
                    }
                    diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                        "${hours}h ago"
                    }
                    diffInMillis < TimeUnit.DAYS.toMillis(7) -> {
                        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                        "${days}d ago"
                    }
                    else -> {
                        val outputFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
                        outputFormat.format(date)
                    }
                }
            } catch (e: Exception) {
                return createdAt
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.notificationId == newItem.notificationId
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }
        }
    }
} 