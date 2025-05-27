package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ItemRequestBinding
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.domain.model.request.RequestType

class RequestListAdapter(
    private val onRequestClicked: (Request) -> Unit
) : PagingDataAdapter<Request, RequestListAdapter.RequestViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            ItemRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        getItem(position)?.let { request ->
            holder.bind(request)
        }
    }

    inner class RequestViewHolder(
        private val binding: ItemRequestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { request ->
                        onRequestClicked(request)
                    }
                }
            }
        }

        fun bind(request: Request) {
            binding.apply {
                tvRequestTitle.text = request.title
                tvRequestContent.text = request.content
                tvApartmentName.text = request.apartmentName
                
                // Set request type icon and text
                when (request.type) {
                    RequestType.REPAIR_AND_MAINTENANCE -> {
                        ivRequestIcon.setImageResource(R.drawable.ic_repair)
                        tvRequestType.text = "Repair & Maintenance"
                    }
                    RequestType.CLEANING -> {
                        ivRequestIcon.setImageResource(R.drawable.ic_cleaning)
                        tvRequestType.text = "Cleaning"
                    }
                    RequestType.FEEDBACK_AND_COMMENTS -> {
                        ivRequestIcon.setImageResource(R.drawable.ic_feedback)
                        tvRequestType.text = "Feedback"
                    }
                    RequestType.OTHER -> {
                        ivRequestIcon.setImageResource(R.drawable.ic_other)
                        tvRequestType.text = "Other"
                    }
                }
                
                // Set status styling
                setupStatusDisplay(request.status)
            }
        }
        
        private fun setupStatusDisplay(status: RequestStatus) {
            binding.apply {
                when (status) {
                    RequestStatus.RECEIVED -> {
                        tvRequestStatus.text = "Received"
                        tvRequestStatus.background = ContextCompat.getDrawable(
                            binding.root.context, 
                            R.drawable.bg_status_received
                        )
                        tvRequestStatus.setTextColor(
                            ContextCompat.getColor(binding.root.context, R.color.status_received_text)
                        )
                    }
                    RequestStatus.IN_PROGRESS -> {
                        tvRequestStatus.text = "In Progress"
                        tvRequestStatus.background = ContextCompat.getDrawable(
                            binding.root.context, 
                            R.drawable.bg_status_in_progress
                        )
                        tvRequestStatus.setTextColor(
                            ContextCompat.getColor(binding.root.context, R.color.status_in_progress_text)
                        )
                    }
                    RequestStatus.RESOLVED -> {
                        tvRequestStatus.text = "Resolved"
                        tvRequestStatus.background = ContextCompat.getDrawable(
                            binding.root.context, 
                            R.drawable.bg_status_resolved
                        )
                        tvRequestStatus.setTextColor(
                            ContextCompat.getColor(binding.root.context, R.color.status_resolved_text)
                        )
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Request>() {
            override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
                return oldItem == newItem
            }
        }
    }
} 