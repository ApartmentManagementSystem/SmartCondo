package com.mit.apartmentmanagement.persentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.data.model.notification.Notification
import com.mit.apartmentmanagement.databinding.ItemNotificationBinding
import com.mit.apartmentmanagement.persentation.util.GenericDiffUtil

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    private var notificationList: List<Notification> = emptyList()
    private var notificationFilterList: List<Notification> = emptyList()

    class MyViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notification) {
            binding.notification = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationFilterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(notificationFilterList[position])
    }

    fun setData(newData: List<Notification>) {
        Log.d("ServiceAdapter", "Number of services received: ${newData.size}")
        val notificationDiffUtil = GenericDiffUtil(
            oldList = notificationList,
            newList = newData,
            areItemsTheSame = { old, new -> old.notificationId == new.notificationId },
            areContentsTheSame = { old, new -> old == new }
        )
        val diffUtilResult = DiffUtil.calculateDiff(notificationDiffUtil)
        notificationList = newData
        notificationFilterList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun filterData(query: String) {
        val filterListNew = if (query.isEmpty()) {
            notificationList
        } else {
            notificationList.filter { notification ->
                notification.title.contains(query, true) || notification.content.contains(
                    query,
                    true
                )
                        || notification.topic.contains(
                    query,
                    true
                ) || notification.createdAt.toString().contains(query, true)
            }
        }
        val notificationDiffUtil = GenericDiffUtil(oldList = notificationFilterList,
            newList = filterListNew,
            areItemsTheSame = { old, new -> old.notificationId == new.notificationId },
            areContentsTheSame = { old, new -> old == new }
        )
        val diffUtilResult = DiffUtil.calculateDiff(notificationDiffUtil)
        notificationFilterList = filterListNew
        diffUtilResult.dispatchUpdatesTo(this)
    }

}