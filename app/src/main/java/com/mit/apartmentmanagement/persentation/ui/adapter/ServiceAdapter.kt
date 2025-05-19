package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemServiceBinding
import com.mit.apartmentmanagement.domain.model.InvoiceType

class ServiceAdapter(
    private val services: List<InvoiceType>,
    private val onServiceClick: (InvoiceType) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount() = services.size

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(service: InvoiceType) {
            binding.apply {
                ivServiceIcon.setImageResource(service.iconRes)
                tvServiceName.text = service.title
                root.setOnClickListener { onServiceClick(service) }
            }
        }
    }
} 