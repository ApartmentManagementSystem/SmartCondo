package com.mit.apartmentmanagement.persentation.ui.invoice.monthly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.facebook.shimmer.ShimmerFrameLayout
import com.mit.apartmentmanagement.databinding.ActivityMonthyInvoiceBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.InvoiceAdapter
import com.mit.apartmentmanagement.persentation.ui.invoice.detail.DetailInvoiceMonthlyActivity
import com.mit.apartmentmanagement.persentation.viewmodels.InvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthlyInvoiceActivity : AppCompatActivity() {
    private val viewModel: InvoiceViewModel by viewModels()
    private val adapter = InvoiceAdapter(onItemClick = { invoice ->
        startActivity(Intent(this, DetailInvoiceMonthlyActivity::class.java).apply {
            putExtra("invoice_id", invoice.monthlyInvoiceId)
        })
    })
    private lateinit var binding: ActivityMonthyInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthyInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        observeInvoices()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        binding.rvInvoices.adapter = adapter

        // Handle loading states with shimmer
        adapter.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.Loading
            val isEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    if (adapter.itemCount == 0) {
                        showShimmer()
                        binding.loadingProgress.visibility = View.GONE
                    } else {
                        binding.swipeRefresh.isRefreshing = true
                    }
                    binding.emptyStateLayout.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    hideShimmer()
                    binding.swipeRefresh.isRefreshing = false
                    binding.loadingProgress.visibility = View.GONE
                    binding.emptyStateLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
                }
                is LoadState.Error -> {
                    hideShimmer()
                    binding.swipeRefresh.isRefreshing = false
                    binding.loadingProgress.visibility = View.GONE
                    binding.emptyStateLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            lifecycleScope.launch {
                text?.toString()?.let { query ->
                    viewModel.search(query.trim())
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
        
        // Set refresh colors to match theme
        binding.swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
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

    private fun showShimmer() {
        val shimmerInvoices = findViewById<ShimmerFrameLayout>(com.mit.apartmentmanagement.R.id.shimmerInvoices)
        
        shimmerInvoices?.visibility = View.VISIBLE
        binding.swipeRefresh.visibility = View.GONE
        shimmerInvoices?.startShimmer()
    }

    private fun hideShimmer() {
        val shimmerInvoices = findViewById<ShimmerFrameLayout>(com.mit.apartmentmanagement.R.id.shimmerInvoices)
        
        shimmerInvoices?.visibility = View.GONE
        binding.swipeRefresh.visibility = View.VISIBLE
        shimmerInvoices?.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) {
            val shimmerInvoices = findViewById<ShimmerFrameLayout>(com.mit.apartmentmanagement.R.id.shimmerInvoices)
            if (shimmerInvoices?.visibility == View.VISIBLE) {
                shimmerInvoices.startShimmer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::binding.isInitialized) {
            val shimmerInvoices = findViewById<ShimmerFrameLayout>(com.mit.apartmentmanagement.R.id.shimmerInvoices)
            shimmerInvoices?.stopShimmer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::binding.isInitialized) {
            val shimmerInvoices = findViewById<ShimmerFrameLayout>(com.mit.apartmentmanagement.R.id.shimmerInvoices)
            shimmerInvoices?.stopShimmer()
        }
    }

}