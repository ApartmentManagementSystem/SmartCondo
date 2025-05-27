package com.mit.apartmentmanagement.persentation.ui.invoice.detail

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityDetailInvoiceMonthlyBinding
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus
import com.mit.apartmentmanagement.persentation.ui.BaseActivity
import com.mit.apartmentmanagement.persentation.ui.adapter.ParkingInvoiceAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.ServiceInvoiceAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.DetailMonthlyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DetailInvoiceMonthlyActivity : BaseActivity() {
    
    private lateinit var binding: ActivityDetailInvoiceMonthlyBinding
    private val viewModel: DetailMonthlyViewModel by viewModels()
    private val parkingInvoiceAdapter = ParkingInvoiceAdapter()
    private val serviceInvoiceAdapter = ServiceInvoiceAdapter()
    
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    
    private var currentInvoice: InvoiceMonthly? = null
    private var paymentDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailInvoiceMonthlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeData()
        loadInvoiceDetails()
    }

    private fun setupUI() {
        setupToolbar()
        setupRecyclerViews()
        setupClickListeners()
        showLoading(true)
    }

    private fun setupToolbar() {
        binding.toolbar.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.title.text = getString(R.string.detail_invoice_title)
    }

    private fun setupRecyclerViews() {
        // Setup parking invoices RecyclerView
        binding.rvParkingInvoices.apply {
            adapter = parkingInvoiceAdapter
            layoutManager = LinearLayoutManager(this@DetailInvoiceMonthlyActivity)
            isNestedScrollingEnabled = false
        }

        // Setup service invoices RecyclerView
        binding.rvServiceInvoices.apply {
            adapter = serviceInvoiceAdapter
            layoutManager = LinearLayoutManager(this@DetailInvoiceMonthlyActivity)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupClickListeners() {
        binding.btnPayNow.setOnClickListener {
            handlePaymentAction()
        }
        
        // Click to expand/collapse parking details
        binding.cardParkingSummary.setOnClickListener {
            toggleParkingDetail()
        }
        
        // Click to expand/collapse service details
        binding.cardServiceSummary.setOnClickListener {
            toggleServiceDetail()
        }
    }
    
    private fun toggleParkingDetail() {
        val isVisible = binding.cardParkingDetail.visibility == View.VISIBLE
        
        // Toggle visibility
        binding.cardParkingDetail.visibility = if (isVisible) View.GONE else View.VISIBLE
        
        // Animate arrow rotation
        val rotation = if (isVisible) 0f else 180f
        binding.ivParkingArrow.animate()
            .rotation(rotation)
            .setDuration(200)
            .start()
    }
    
    private fun toggleServiceDetail() {
        val isVisible = binding.cardServiceDetail.visibility == View.VISIBLE
        
        // Toggle visibility
        binding.cardServiceDetail.visibility = if (isVisible) View.GONE else View.VISIBLE
        
        // Animate arrow rotation
        val rotation = if (isVisible) 0f else 180f
        binding.ivServiceArrow.animate()
            .rotation(rotation)
            .setDuration(200)
            .start()
    }

    private fun loadInvoiceDetails() {
        val invoiceId = intent.getStringExtra(EXTRA_INVOICE_ID)
        if (invoiceId.isNullOrEmpty()) {
            showError("Invoice ID not found")
            finish()
            return
        }
        
        Log.d(TAG, "Loading invoice details for ID: $invoiceId")
        viewModel.getInvoiceDetails(invoiceId)
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.invoiceDetails.collectLatest { invoice ->
                invoice?.let { 
                    Log.d(TAG, "Received invoice details: $invoice")
                    currentInvoice = it
                    bindInvoiceDetails(it)
                    showLoading(false)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                showLoading(isLoading)
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let { 
                    Log.e(TAG, "Error loading invoice details: $error")
                    showError(it)
                    showLoading(false)
                    viewModel.clearError()
                }
            }
        }
        
        // Observe payment loading
        lifecycleScope.launch {
            viewModel.isPaymentLoading.collectLatest { isLoading ->
                updatePaymentButtonLoading(isLoading)
            }
        }
        
        // Observe payment success
        lifecycleScope.launch {
            viewModel.paymentSuccess.collectLatest { message ->
                message?.let {
                    dismissPaymentDialog()
                    showSuccess(it)
                    viewModel.clearPaymentMessages()
                }
            }
        }
        
        // Observe payment error
        lifecycleScope.launch {
            viewModel.paymentError.collectLatest { error ->
                error?.let {
                    dismissPaymentDialog()
                    showError(it)
                    viewModel.clearPaymentMessages()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.nestedScrollView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun bindInvoiceDetails(invoice: InvoiceMonthly) {
        try {
            Log.d(TAG, "Binding invoice details")
            bindHeaderSection(invoice)
            bindElectricitySection(invoice)
            bindWaterSection(invoice)
            bindParkingSection(invoice)
            bindServiceSection(invoice)
            bindPaymentInfoSection(invoice)
            updatePaymentButton(invoice)
        } catch (e: Exception) {
            Log.e(TAG, "Error binding invoice details", e)
            showError("Error displaying invoice details: ${e.message}")
        }
    }

    private fun bindHeaderSection(invoice: InvoiceMonthly) {
        // Use invoiceTime directly as it's already a String in format MM/yyyy
        val monthYear = invoice.invoiceTime
        
        binding.apply {
            tvInvoiceTitle.text = getString(R.string.monthly_invoice_format, monthYear)
            tvApartmentName.text = getString(R.string.apartment_format, invoice.apartmentName)
            tvInvoiceId.text = getString(R.string.invoice_id_format, invoice.monthlyInvoiceId)
            tvTotalAmount.text = formatCurrency(invoice.totalPrice)
            
            // Update status
            updateStatusDisplay(invoice.status)
        }
    }

    private fun updateStatusDisplay(status: PaymentStatus) {
        binding.tvStatus.apply {
            when (status) {
                PaymentStatus.PAID -> {
                    text = getString(R.string.status_paid)
                    background = ContextCompat.getDrawable(context, R.drawable.bg_status_paid)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                PaymentStatus.UNPAID -> {
                    text = getString(R.string.status_unpaid)
                    background = ContextCompat.getDrawable(context, R.drawable.bg_status_unpaid)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                else -> {
                    text = getString(R.string.status_pending)
                    background = ContextCompat.getDrawable(context, R.drawable.bg_status_unpaid)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }

    private fun bindElectricitySection(invoice: InvoiceMonthly) {
        binding.apply {
            tvElectricityAmount.text = formatCurrency(invoice.electricInvoice.totalPrice ?: 0.0)
            
            // Format electricity usage details
            val electricityDetail = invoice.electricInvoice.let {
                val usage = it.total ?: 0
                val unitPrice = it.unitPrice ?: 0.0
                "$usage kWh × ${formatCurrency(unitPrice)}/kWh"
            }
            tvElectricityDetail.text = electricityDetail
        }
    }

    private fun bindWaterSection(invoice: InvoiceMonthly) {
        binding.apply {
            tvWaterAmount.text = formatCurrency(invoice.waterInvoice.totalPrice ?: 0.0)
            
            // Format water usage details
            val waterDetail = invoice.waterInvoice.let {
                val usage = it.total
                val unitPrice = it.unitPrice
                "$usage m³ × ${formatCurrency(unitPrice)}/m³"
            }
            tvWaterDetail.text = waterDetail
        }
    }

    private fun bindParkingSection(invoice: InvoiceMonthly) {
        if (invoice.vehicleInvoices.isNotEmpty()) {
            // Show parking summary card
            binding.cardParkingSummary.visibility = View.VISIBLE
            val totalParkingAmount = invoice.vehicleInvoices.sumOf { it.unitPrice ?: 0.0 }
            binding.tvParkingAmount.text = formatCurrency(totalParkingAmount)
            binding.tvParkingDetail.text = "${invoice.vehicleInvoices.size} phương tiện"
            
            // Hide parking detail card initially (user can click to show)
            binding.cardParkingDetail.visibility = View.GONE
            parkingInvoiceAdapter.submitList(invoice.vehicleInvoices)
        } else {
            binding.cardParkingSummary.visibility = View.GONE
            binding.cardParkingDetail.visibility = View.GONE
        }
    }

    private fun bindServiceSection(invoice: InvoiceMonthly) {
        if (invoice.serviceInvoices.isNotEmpty()) {
            // Show service summary card
            binding.cardServiceSummary.visibility = View.VISIBLE
            val totalServiceAmount = invoice.serviceInvoices.sumOf { it.unitPrice ?: 0.0 }
            binding.tvServiceAmount.text = formatCurrency(totalServiceAmount)
            binding.tvServiceDetail.text = "${invoice.serviceInvoices.size} dịch vụ"
            
            // Hide service detail card initially (user can click to show)
            binding.cardServiceDetail.visibility = View.GONE
            serviceInvoiceAdapter.submitList(invoice.serviceInvoices)
        } else {
            binding.cardServiceSummary.visibility = View.GONE
            binding.cardServiceDetail.visibility = View.GONE
        }
    }

    private fun bindPaymentInfoSection(invoice: InvoiceMonthly) {
        binding.apply {
            // Created date - use invoiceTime directly
            tvCreatedDate.text = invoice.invoiceTime
            
            // Paid date (show only if paid)
            if (invoice.status == PaymentStatus.PAID) {
                layoutPaidDate.visibility = View.VISIBLE
                tvPaidDate.text = formatDate(invoice.paymentDate)
            } else {
                layoutPaidDate.visibility = View.GONE
            }
            
            // Due date
            tvDueDate.text = formatDate(invoice.dueDate)
        }
    }

    private fun updatePaymentButton(invoice: InvoiceMonthly) {
        binding.btnPayNow.apply {
            when (invoice.status) {
                PaymentStatus.PAID -> {
                    text = getString(R.string.already_paid)
                    isEnabled = false
                    alpha = 0.6f
                    visibility = View.GONE // Ẩn button khi đã thanh toán
                }
                PaymentStatus.UNPAID -> {
                    text = getString(R.string.pay_now)
                    isEnabled = true
                    alpha = 1.0f
                    visibility = View.VISIBLE
                }
                else -> {
                    text = getString(R.string.processing)
                    isEnabled = false
                    alpha = 0.6f
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handlePaymentAction() {
        currentInvoice?.let { invoice ->
            if (invoice.status == PaymentStatus.UNPAID) {
                showPaymentConfirmationDialog(invoice)
            }
        }
    }
    
    private fun showPaymentConfirmationDialog(invoice: InvoiceMonthly) {
        try {
            val dialogView = layoutInflater.inflate(R.layout.dialog_payment_confirmation, null)
            
            paymentDialog = Dialog(this).apply {
                setContentView(dialogView)
                setCancelable(true)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
            
            // Setup dialog views
            val tvPaymentAmount = dialogView.findViewById<TextView>(R.id.tv_payment_amount)
            val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)
            val btnConfirm = dialogView.findViewById<MaterialButton>(R.id.btn_confirm)
            
            // Set amount
            tvPaymentAmount.text = formatCurrency(invoice.totalPrice)
            
            // Setup button listeners
            btnCancel.setOnClickListener {
                dismissPaymentDialog()
            }
            
            btnConfirm.setOnClickListener {
                processPayment(invoice.monthlyInvoiceId)
            }
            
            paymentDialog?.show()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error showing payment dialog", e)
            showError("Không thể hiển thị dialog thanh toán")
        }
    }
    
    private fun processPayment(invoiceId: String) {
        Log.d(TAG, "Processing payment for invoice: $invoiceId")
        viewModel.payInvoice(invoiceId)
    }
    
    private fun dismissPaymentDialog() {
        paymentDialog?.dismiss()
        paymentDialog = null
    }
    
    private fun updatePaymentButtonLoading(isLoading: Boolean) {
        binding.btnPayNow.apply {
            if (isLoading) {
                text = "Đang xử lý..."
                isEnabled = false
                alpha = 0.6f
            } else {
                currentInvoice?.let { updatePaymentButton(it) }
            }
        }
    }

    private fun formatCurrency(amount: Double): String {
        return try {
            currencyFormatter.format(amount).replace("₫", "VND")
        } catch (e: Exception) {
            "${amount.toLong()} VND"
        }
    }
    
    private fun formatDate(date: Date?): String {
        return date?.let { dateFormatter.format(it) } ?: "N/A"
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.error))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
        Log.e(TAG, message)
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    
    private fun showSuccess(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.success_green))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        dismissPaymentDialog()
    }

    companion object {
        private const val TAG = "DetailInvoiceActivity"
        const val EXTRA_INVOICE_ID = "invoice_id"
        
        // Extension function for easy intent creation
        fun createIntent(context: android.content.Context, invoiceId: String): android.content.Intent {
            return android.content.Intent(context, DetailInvoiceMonthlyActivity::class.java).apply {
                putExtra(EXTRA_INVOICE_ID, invoiceId)
            }
        }
    }
}