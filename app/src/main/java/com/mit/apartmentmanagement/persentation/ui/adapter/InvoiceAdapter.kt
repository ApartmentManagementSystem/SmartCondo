package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemInvoiceBinding
import com.mit.apartmentmanagement.domain.model.PaymentStatus
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import java.time.format.DateTimeFormatter

class InvoiceAdapter(
    private val onItemClick: (InvoiceMonthly) -> Unit
) : PagingDataAdapter<InvoiceMonthly, InvoiceAdapter.InvoiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = getItem(position)
        invoice?.let { holder.bind(it) }
    }

    inner class InvoiceViewHolder(private val binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: InvoiceMonthly) {
            val formatter = DateTimeFormatter.ofPattern("MM/yyyy")
            val monthYear = invoice.billingTime.format(formatter)
            
            binding.apply {
                tvInvoiceTitle.text = "Hóa đơn tháng $monthYear"
                tvInvoiceId.text = "Mã đơn: ${invoice.monthlyInvoiceId}"
                tvStatus.text = if (invoice.status == PaymentStatus.PAID) "Đã thanh toán" else "Chưa thanh toán"
                tvStatus.setTextColor(
                    if (invoice.status == PaymentStatus.UNPAID)
                        android.graphics.Color.parseColor("#4CAF50") // Green
                    else 
                        android.graphics.Color.parseColor("#F44336") // Red
                )
                
                // Calculate total amount from all invoice types
                val totalAmount = calculateTotalAmount(invoice)
                tvAmount.text = "$$totalAmount"

                // Handle click event
                root.setOnClickListener { onItemClick(invoice) }
            }
        }
        
        private fun calculateTotalAmount(invoice: InvoiceMonthly): Int {
            // TODO: Implement actual calculation based on your business logic
            return 480 // Placeholder amount
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<InvoiceMonthly>() {
            override fun areItemsTheSame(oldItem: InvoiceMonthly, newItem: InvoiceMonthly): Boolean {
                return oldItem.monthlyInvoiceId == newItem.monthlyInvoiceId
            }

            override fun areContentsTheSame(oldItem: InvoiceMonthly, newItem: InvoiceMonthly): Boolean {
                return oldItem == newItem
            }
        }
    }
} 