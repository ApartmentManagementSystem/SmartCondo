package com.mit.apartmentmanagement.persentation.ui.invoice.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mit.apartmentmanagement.databinding.ActivityDetailMonthlyBinding
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus
import com.mit.apartmentmanagement.persentation.ui.adapter.ParkingInvoiceAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.ServiceInvoiceAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.DetailMonthlyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class DetailMonthlyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMonthlyBinding
    private val viewModel: DetailMonthlyViewModel by viewModels()
    private val parkingInvoiceAdapter = ParkingInvoiceAdapter()
    private val serviceInvoiceAdapter = ServiceInvoiceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailMonthlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        observeInvoiceDetails()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerViews() {
        binding.rvParkingInvoices.adapter = parkingInvoiceAdapter
        binding.rvServiceInvoices.adapter = serviceInvoiceAdapter
    }

    private fun observeInvoiceDetails() {
        val invoiceId = intent.getStringExtra("invoice_id") ?: return
        viewModel.getInvoiceDetails(invoiceId)
        lifecycleScope.launch {
            viewModel.invoiceDetails.collectLatest { invoice ->
                invoice?.let { bindInvoiceDetails(it) }
            }
        }
    }

    private fun bindInvoiceDetails(invoice: com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly) {
        val formatter = DateTimeFormatter.ofPattern("MM/yyyy")
        val monthYear = invoice.invoiceTime.format(formatter)

        binding.apply {
            // Header section
            tvInvoiceTitle.text = "Hóa đơn tháng $monthYear"
            tvInvoiceId.text = "Mã đơn: ${invoice.monthlyInvoiceId}"
            tvApartmentName.text = "Căn hộ: ${invoice.apartmentName}"
            tvStatus.text = if (invoice.status == PaymentStatus.PAID) "Đã thanh toán" else "Chưa thanh toán"
            tvStatus.setTextColor(
                if (invoice.status == PaymentStatus.PAID)
                    android.graphics.Color.parseColor("#4CAF50") // Green
                else 
                    android.graphics.Color.parseColor("#F44336") // Red
            )

            // Electricity section
            tvElectricityAmount.text = "Số tiền: $${invoice.electricInvoice.totalPrice}"

            // Water section
            tvWaterAmount.text = "Số tiền: $${invoice.waterInvoice.totalPrice}"

            // Parking section
            parkingInvoiceAdapter.submitList(invoice.vehicleInvoices)

            // Service section
            serviceInvoiceAdapter.submitList(invoice.serviceInvoices)

            // Total amount
            tvTotalAmount.text = "$${invoice.totalPrice}"
        }
    }
}