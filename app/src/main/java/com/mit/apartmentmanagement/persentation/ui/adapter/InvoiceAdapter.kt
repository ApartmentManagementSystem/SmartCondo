package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemMonthlyInvoiceBinding
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import java.time.format.DateTimeFormatter

class InvoiceAdapter(
    private val onItemClick: (InvoiceMonthly) -> Unit
) : PagingDataAdapter<InvoiceMonthly, InvoiceAdapter.InvoiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = ItemMonthlyInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = getItem(position)
        invoice?.let { holder.bind(it) }
    }

    inner class InvoiceViewHolder(private val binding: ItemMonthlyInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: InvoiceMonthly) {
            val formatter = DateTimeFormatter.ofPattern("MM/yyyy")
            val monthYear = invoice.invoiceTime.format(formatter)
            
            binding.apply {
                tvInvoiceTitle.text = "Hóa đơn tháng $monthYear"
                tvInvoiceId.text = "Mã đơn: ${invoice.monthlyInvoiceId}"
                tvStatus.text = if (invoice.status == PaymentStatus.PAID) "Đã thanh toán" else "Chưa thanh toán"
                tvStatus.setTextColor(
                    if (invoice.status == PaymentStatus.PAID)
                        android.graphics.Color.parseColor("#4CAF50") // Green
                    else 
                        android.graphics.Color.parseColor("#F44336") // Red
                )
                
                tvAmount.text = "$${invoice.totalPrice}"

                // Handle click event
                root.setOnClickListener { onItemClick(invoice) }
            }
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