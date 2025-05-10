package com.mit.apartmentmanagement.persentation.ui.invoice.electricity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mit.apartmentmanagement.databinding.ActivityElectricityInvoiceBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.InvoiceAdapter
import com.mit.apartmentmanagement.persentation.ui.invoice.detail.DetailMonthlyActivity
import com.mit.apartmentmanagement.persentation.viewmodels.ElectricityInvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ElectricityInvoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityElectricityInvoiceBinding
    private val viewModel: ElectricityInvoiceViewModel by viewModels()
    private val adapter = InvoiceAdapter(
        onItemClick = { invoice ->
            startActivity(Intent(this, DetailMonthlyActivity::class.java).apply {
                putExtra("invoice_id", invoice.monthlyInvoiceId)
            })
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElectricityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSwipeRefresh()
        observeInvoices()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        binding.rvInvoices.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeInvoices() {
        lifecycleScope.launch {
            viewModel.electricityInvoices.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
} 