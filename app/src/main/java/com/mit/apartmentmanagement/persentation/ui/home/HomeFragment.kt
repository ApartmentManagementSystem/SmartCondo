package com.mit.apartmentmanagement.persentation.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.FragmentHomeBinding
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.persentation.ui.ApartmentDetailActivity
import com.mit.apartmentmanagement.persentation.ui.adapter.AmenityAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.HomeViewModel
import com.mit.apartmentmanagement.persentation.ui.adapter.ApartmentAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationAdapter
import com.mit.apartmentmanagement.persentation.ui.notification.NotificationDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.util.Timer
import java.util.TimerTask
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var amenityAdapter: AmenityAdapter
    private lateinit var apartmentAdapter: ApartmentAdapter
    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationTimer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
        setupSwipeRefresh()
        updateHeaderBackground()
    }

    private fun setupUI() {
        // Setup apartments recycler view
        apartmentAdapter = ApartmentAdapter(
            onApartmentClicked = {
                val intent = Intent(requireContext(), ApartmentDetailActivity::class.java)
                intent.putExtra("apartment", it)
                startActivity(intent)
            }
        )
        binding.apartmentsRecyclerView.adapter = apartmentAdapter

        // Setup notifications view pager
        notificationAdapter = NotificationAdapter(onNotificationClicked = {
            val intent= Intent(requireContext(), NotificationDetailActivity::class.java)
            intent.putExtra("notification", it)
        })

        binding.notificationsViewPager.adapter = notificationAdapter
        setupAutomaticNotificationScroll()

        // Setup amenities recycler view
        amenityAdapter = AmenityAdapter(onAmenityClicked = {
            val intent = Intent(requireContext(), ApartmentDetailActivity::class.java)
            intent.putExtra("amenity", it)
            startActivity(intent)
        })
        binding.amenitiesRecyclerView.adapter = amenityAdapter

        // Setup bills chart
        setupInvoiceChart()
    }

    private fun setupObservers() {
        viewModel.apartments.observe(viewLifecycleOwner) { apartments ->
            apartmentAdapter.submitList(apartments)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notifications.collect { notifications ->
                notificationAdapter.submitData(notifications)
            }
        }

        viewModel.amenities.observe(viewLifecycleOwner) { amenities ->
            amenityAdapter.submitList(amenities)
        }

        viewModel.invoices.observe(viewLifecycleOwner) { invoices ->
            updateInvoicesChart(invoices)
        }

        viewModel.greeting.observe(viewLifecycleOwner) { greeting ->
            binding.greetingText.text = greeting
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupAutomaticNotificationScroll() {
        notificationTimer?.cancel()
        notificationTimer = Timer()
        notificationTimer?.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    if ((binding.notificationsViewPager.adapter?.itemCount ?: 0) > 0) {
                        binding.notificationsViewPager.currentItem += 1
                    }
                }
            }
        }, 3000, 3000)
    }

    private fun setupInvoiceChart() {
        binding.invoicesChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            setDrawBorders(false)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }
            
            axisLeft.apply {
                setDrawGridLines(true)
                setDrawAxisLine(false)
            }
            
            axisRight.isEnabled = false
            
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
        }
    }

    private fun updateInvoicesChart(invoices: List<InvoiceMonthly>) {
        val entries = invoices.mapIndexed { index, invoice ->
            BarEntry(index.toFloat(), invoice.totalPrice.toFloat())
        }

        val months = invoices.map { it.invoiceTime }
        
        val dataSet = BarDataSet(entries, "Invoice").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        binding.invoicesChart.apply {
            data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(months)
            invalidate()
        }
    }

    private fun updateHeaderBackground() {
        val currentTime = LocalTime.now()
        val backgroundResId = when {
            currentTime.hour in 5..11 -> R.drawable.bg_morning
            currentTime.hour in 12..17 -> R.drawable.bg_afternoon
            else -> R.drawable.bg_evening
        }
        binding.headerBackground.setBackgroundResource(backgroundResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notificationTimer?.cancel()
        notificationTimer = null
        _binding = null
    }
} 