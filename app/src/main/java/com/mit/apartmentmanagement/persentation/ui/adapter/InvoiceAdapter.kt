package com.mit.apartmentmanagement.persentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemMonthlyInvoiceBinding
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class InvoiceAdapter(
    private val onItemClick: (InvoiceMonthly) -> Unit
) : PagingDataAdapter<InvoiceMonthly, InvoiceAdapter.InvoiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = ItemMonthlyInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = getItem(position)
        invoice?.let { 
            try {
                holder.bind(it)
            } catch (e: Exception) {
                Log.e("InvoiceAdapter", "Error binding invoice at position $position", e)
            }
        }
    }

    inner class InvoiceViewHolder(private val binding: ItemMonthlyInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: InvoiceMonthly) {
            try {
                // Parse invoiceTime which is a string in format MM/yyyy
                val monthYear = invoice.invoiceTime
                
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
                    
                    // Format currency
                    val numberFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
                    tvAmount.text = numberFormat.format(invoice.totalPrice)
                    
                    tvApartmentName.text = invoice.apartmentName

                    // Handle click event
                    root.setOnClickListener { onItemClick(invoice) }
                }
                
                Log.d("InvoiceAdapter", "Successfully bound invoice ${invoice.monthlyInvoiceId}")
            } catch (e: Exception) {
                Log.e("InvoiceAdapter", "Error binding invoice", e)
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