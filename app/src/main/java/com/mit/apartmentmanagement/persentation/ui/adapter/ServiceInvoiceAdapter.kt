package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemServiceInvoiceBinding
import com.mit.apartmentmanagement.domain.model.invoice.ServiceInvoice
import java.text.NumberFormat
import java.util.Locale

class ServiceInvoiceAdapter : ListAdapter<ServiceInvoice, ServiceInvoiceAdapter.ServiceInvoiceViewHolder>(DIFF_CALLBACK) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceInvoiceViewHolder {
        val binding = ItemServiceInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceInvoiceViewHolder, position: Int) {
        holder.bind(getItem(position), currencyFormatter)
    }

    class ServiceInvoiceViewHolder(private val binding: ItemServiceInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: ServiceInvoice, currencyFormatter: NumberFormat) {
            binding.apply {
                tvServiceType.text = invoice.type.name
                tvServiceAmount.text = formatCurrency(invoice.unitPrice, currencyFormatter)
            }
        }

        private fun formatCurrency(amount: Double, formatter: NumberFormat): String {
            return try {
                formatter.format(amount).replace("₫", "VND")
            } catch (e: Exception) {
                "${amount.toLong()} VND"
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