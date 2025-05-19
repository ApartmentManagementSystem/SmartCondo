package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemServiceInvoiceBinding
import com.mit.apartmentmanagement.domain.model.invoice.ServiceInvoice

class ServiceInvoiceAdapter : ListAdapter<ServiceInvoice, ServiceInvoiceAdapter.ServiceInvoiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceInvoiceViewHolder {
        val binding = ItemServiceInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceInvoiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ServiceInvoiceViewHolder(private val binding: ItemServiceInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: ServiceInvoice) {
            binding.apply {
                tvServiceType.text = invoice.type.name
                tvServiceAmount.text = "Số tiền: $${invoice.totalPrice}"
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ServiceInvoice>() {
            override fun areItemsTheSame(oldItem: ServiceInvoice, newItem: ServiceInvoice): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ServiceInvoice, newItem: ServiceInvoice): Boolean {
                return oldItem == newItem
            }
        }
    }
} 