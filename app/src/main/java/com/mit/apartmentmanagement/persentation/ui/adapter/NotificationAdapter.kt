package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ItemNotificationBinding
import com.mit.apartmentmanagement.domain.model.Notification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationAdapter(
    private val onNotificationClicked: (Notification) -> Unit
) : PagingDataAdapter<Notification, NotificationAdapter.NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(
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
        private val binding: ItemNotificationBinding
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
        }

        fun bind(notification: Notification) {
            binding.apply {
                notificationTitle.text = notification.title
                notificationContent.text = notification.content
                notificationTime.text = getFormattedTime(notification.createdAt)

                // Set background color based on read status
                val backgroundColor = if (notification.isRead) {
                    R.color.notification_read_background
                } else {
                    R.color.notification_unread_background
                }
                root.setCardBackgroundColor(
                    ContextCompat.getColor(root.context, backgroundColor)
                )

                // Set unread indicator
                notificationUnreadIndicator.isVisible = !notification.isRead
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
                        "$minutes minutes ago"
                    }
                    diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                        "$hours hours ago"
                    }
                    diffInMillis < TimeUnit.DAYS.toMillis(7) -> {
                        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                        "$days days ago"
                    }
                    else -> {
                        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
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