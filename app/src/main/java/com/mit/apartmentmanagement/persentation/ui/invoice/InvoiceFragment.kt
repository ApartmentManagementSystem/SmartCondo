package com.mit.apartmentmanagement.persentation.ui.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.mit.apartmentmanagement.databinding.FragmentInvoiceBinding
import com.mit.apartmentmanagement.domain.model.InvoiceType
import com.mit.apartmentmanagement.persentation.ui.adapter.InvoiceAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.ServiceAdapter
import com.mit.apartmentmanagement.persentation.ui.invoice.detail.DetailInvoiceMonthlyActivity
import com.mit.apartmentmanagement.persentation.ui.invoice.electricity.ElectricityInvoiceActivity
import com.mit.apartmentmanagement.persentation.ui.invoice.monthly.MonthlyInvoiceActivity
import com.mit.apartmentmanagement.persentation.ui.invoice.parking.ParkingInvoiceActivity
import com.mit.apartmentmanagement.persentation.ui.invoice.water.WaterInvoiceActivity
import com.mit.apartmentmanagement.persentation.util.SpaceItemDecoration
import com.mit.apartmentmanagement.persentation.viewmodels.InvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InvoiceViewModel by viewModels()
    private val invoiceAdapter = InvoiceAdapter(
        onItemClick = { invoice ->
            startActivity(Intent(requireContext(), DetailInvoiceMonthlyActivity::class.java).apply {
                putExtra("invoice_id", invoice.monthlyInvoiceId)
            })
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupServices()
        setupInvoices()
        setupViewAll()
        observeInvoices()
    }

    private fun setupServices() {
        val services = InvoiceType.entries
        val serviceAdapter = ServiceAdapter(services) { service ->
            when (service) {
                InvoiceType.ELECTRICITY -> startActivity(
                    Intent(
                        requireContext(),
                        ElectricityInvoiceActivity::class.java
                    )
                )

                InvoiceType.WATER -> startActivity(
                    Intent(
                        requireContext(),
                        WaterInvoiceActivity::class.java
                    )
                )

                InvoiceType.PARKING -> startActivity(
                    Intent(
                        requireContext(),
                        ParkingInvoiceActivity::class.java
                    )
                )

                InvoiceType.SERVICE -> {
                    // Handle common service click
                }
            }
        }
        binding.rvServices.addItemDecoration(
            SpaceItemDecoration(
                requireContext(),
                8,
                8,
                serviceAdapter.itemCount
            )
        )
        binding.rvServices.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = serviceAdapter
        }
    }

    private fun setupInvoices() {
        binding.rvInvoices.adapter = invoiceAdapter
        
        // Handle loading states with shimmer
        invoiceAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    showShimmer()
                }
                is LoadState.NotLoading -> {
                    hideShimmer()
                }
                is LoadState.Error -> {
                    hideShimmer()
                }
            }
        }
    }

    private fun setupViewAll() {
        binding.tvViewAll.setOnClickListener {
            startActivity(Intent(requireContext(), MonthlyInvoiceActivity::class.java))
        }
    }

    private fun observeInvoices() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allInvoices.collectLatest { pagingData ->
                invoiceAdapter.submitData(pagingData)
                Log.d("InvoiceFragment", "Observed invoices: $pagingData")
            }
        }
    }


    private fun showShimmer() {
       val shimmerInvoices=binding.shimmerInvoices
         binding.rvInvoices.visibility= View.GONE
        binding.shimmerInvoices.shimmerFrameLayout.visibility = View.VISIBLE
        shimmerInvoices.shimmerFrameLayout.startShimmer()
    }

    private fun hideShimmer() {
        val shimmerServices = binding.shimmerInvoices
        binding.rvInvoices.visibility = View.VISIBLE
        binding.shimmerInvoices.shimmerFrameLayout.visibility = View.GONE
        shimmerServices.shimmerFrameLayout.stopShimmer()
    }
}