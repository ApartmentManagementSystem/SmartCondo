package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemParkingInvoiceBinding
import com.mit.apartmentmanagement.domain.model.invoice.ParkingInvoice
import java.text.NumberFormat
import java.util.Locale

class ParkingInvoiceAdapter : ListAdapter<ParkingInvoice, ParkingInvoiceAdapter.ParkingInvoiceViewHolder>(DIFF_CALLBACK) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingInvoiceViewHolder {
        val binding = ItemParkingInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkingInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkingInvoiceViewHolder, position: Int) {
        holder.bind(getItem(position), currencyFormatter)
    }

    class ParkingInvoiceViewHolder(private val binding: ItemParkingInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: ParkingInvoice, currencyFormatter: NumberFormat) {
            binding.apply {
                tvVehicleType.text = invoice.type
                tvVehicleNumber.text = "Biển số: ${invoice.licensePlate}"
                tvParkingAmount.text = formatCurrency(invoice.unitPrice, currencyFormatter)
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ParkingInvoice>() {
            override fun areItemsTheSame(oldItem: ParkingInvoice, newItem: ParkingInvoice): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ParkingInvoice, newItem: ParkingInvoice): Boolean {
                return oldItem == newItem
            }
        }
    }
} 