package com.mit.apartmentmanagement.persentation.ui.invoice.monthly

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.mit.apartmentmanagement.databinding.ActivityMonthyInvoiceBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.InvoiceAdapter
import com.mit.apartmentmanagement.persentation.ui.invoice.detail.DetailMonthlyActivity
import com.mit.apartmentmanagement.persentation.viewmodels.InvoiceViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MonthlyInvoiceActivity : AppCompatActivity() {
    private val viewModel: InvoiceViewModel by viewModels()
    private val adapter = InvoiceAdapter(onItemClick = { invoice ->
        startActivity(Intent(this, DetailMonthlyActivity::class.java).apply {
            putExtra("invoice_id", invoice.monthlyInvoiceId)
        })
    })
    private lateinit var binding: ActivityMonthyInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMonthyInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        observeInvoices()
    }

    private fun setupRecyclerView() {
        binding.rvInvoices.adapter = adapter

        // Handle loading states
        adapter.addLoadStateListener { loadState ->
            binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            lifecycleScope.launch {
                text?.toString()?.let { query ->
                    if (query.isNotEmpty()) {
                        viewModel.search(query)
                    } else {
                        observeInvoices()
                    }
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeInvoices() {
        lifecycleScope.launch {
            viewModel.allInvoices
                .distinctUntilChanged()
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

}