package com.mit.apartmentmanagement.persentation.ui.invoice.monthly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.mit.apartmentmanagement.R
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

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: InvoiceViewModel by viewModels()
    private val adapter = InvoiceAdapter(onItemClick = { invoice ->
        val intent=Intent(this, DetailInvoiceMonthlyActivity::class.java)
           intent. putExtra("invoice_id", invoice.monthlyInvoiceId)
        resultLauncher.launch(intent)
        })
    private lateinit var binding: ActivityMonthyInvoiceBinding
    private var shimmerInvoices: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthyInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadData()
            }
        }
        
        shimmerInvoices = findViewById(R.id.shimmerInvoices)
        
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        observeInvoices()
    }

    private fun loadData() {
        TODO("Not yet implemented")
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        // Explicitly set LayoutManager to ensure proper functioning
        binding.rvInvoices.layoutManager = LinearLayoutManager(this)
        binding.rvInvoices.adapter = adapter
        
        // Enable nested scrolling for better performance with paging
        binding.rvInvoices.isNestedScrollingEnabled = true

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
                    
                    // Show empty state only if there are no items and no error
                    if (isEmpty) {
                        binding.emptyStateLayout.visibility = View.VISIBLE
                        Log.d("MonthlyInvoiceActivity", "No items in adapter")
                    } else {
                        binding.emptyStateLayout.visibility = View.GONE
                        Log.d("MonthlyInvoiceActivity", "Items loaded: ${adapter.itemCount}")
                    }
                }
                is LoadState.Error -> {
                    val error = (loadState.refresh as LoadState.Error).error
                    hideShimmer()
                    binding.swipeRefresh.isRefreshing = false
                    binding.loadingProgress.visibility = View.GONE
                    binding.emptyStateLayout.visibility = View.VISIBLE
                    Log.e("MonthlyInvoiceActivity", "Error loading invoices", error)
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
                    Log.d("MonthlyInvoiceActivity", "Collecting new paging data")
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun showShimmer() {
        shimmerInvoices?.let {
            it.visibility = View.VISIBLE
            binding.swipeRefresh.visibility = View.GONE
            it.startShimmer()
        }
    }

    private fun hideShimmer() {
        shimmerInvoices?.let {
            it.visibility = View.GONE
            binding.swipeRefresh.visibility = View.VISIBLE
            it.stopShimmer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) {
            shimmerInvoices?.let { 
                if (it.visibility == View.VISIBLE) {
                    it.startShimmer()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        shimmerInvoices?.stopShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        shimmerInvoices?.stopShimmer()
    }
}