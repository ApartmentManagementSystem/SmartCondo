package com.mit.apartmentmanagement.persentation.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.FragmentHomeBinding
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.persentation.ui.ApartmentDetailActivity
import com.mit.apartmentmanagement.persentation.ui.adapter.AmenityAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.HomeViewModel
import com.mit.apartmentmanagement.persentation.ui.adapter.ApartmentAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationViewPagerAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.InvoiceChartPagerAdapter
import com.mit.apartmentmanagement.persentation.ui.notification.NotificationDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var amenityAdapter: AmenityAdapter
    private lateinit var apartmentAdapter: ApartmentAdapter
    private lateinit var notificationViewPagerAdapter: NotificationViewPagerAdapter
    private lateinit var invoiceChartPagerAdapter: InvoiceChartPagerAdapter
    private lateinit var bottomNav: BottomNavigationView
    
    // Auto-scroll handling
    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private var autoScrollRunnable: Runnable? = null
    private val autoScrollDelay = 4000L
    private var isUserInteracting = false
    
    // Page indicators
    private val pageIndicators = mutableListOf<ImageView>()
    private val chartPageIndicators = mutableListOf<ImageView>()

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
        bottomNav= requireActivity().findViewById(R.id.bottom_nav_menu)

        // Setup apartments recycler view
        apartmentAdapter = ApartmentAdapter(
            onApartmentClicked = {
                val intent = Intent(requireContext(), ApartmentDetailActivity::class.java)
                intent.putExtra("apartment", it)
                startActivity(intent)
            }
        )
        binding.apartmentsRecyclerView.adapter = apartmentAdapter

        // Setup notifications view pager with new adapter
        notificationViewPagerAdapter = NotificationViewPagerAdapter(onNotificationClicked = {
            val intent = Intent(requireContext(), NotificationDetailActivity::class.java)
            intent.putExtra("notification", it)
            startActivity(intent)
        })

        binding.notificationsViewPager.adapter = notificationViewPagerAdapter
        setupNotificationViewPager()

        // Setup amenities recycler view
        amenityAdapter = AmenityAdapter(onAmenityClicked = {
            val intent = Intent(requireContext(), ApartmentDetailActivity::class.java)
            intent.putExtra("amenity", it)
            startActivity(intent)
        })
        binding.amenitiesRecyclerView.adapter = amenityAdapter

        binding.seeAllNotification.setOnClickListener {
            navigateNotificationFragment()
        }

        // Setup invoice charts ViewPager2
        invoiceChartPagerAdapter = InvoiceChartPagerAdapter()
        binding.invoiceChartsViewPager.adapter = invoiceChartPagerAdapter
        setupInvoiceChartsViewPager()
    }

    private fun navigateNotificationFragment() {
        bottomNav.selectedItemId = R.id.notificationFragment
    }

    private fun setupNotificationViewPager() {
        binding.notificationsViewPager.apply {
            // Enable smooth scrolling
            offscreenPageLimit = 1
            
            // Add page transformer for smooth transitions
            setPageTransformer { page, position ->
                page.apply {
                    when {
                        position < -1 -> alpha = 0f
                        position <= 1 -> {
                            alpha = 1f
                            translationX = 0f
                            scaleX = 1f
                            scaleY = 1f
                        }
                        else -> alpha = 0f
                    }
                }
            }
            
            // Register page change callback to handle user interaction
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        ViewPager2.SCROLL_STATE_DRAGGING -> {
                            isUserInteracting = true
                            stopAutoScroll()
                        }
                        ViewPager2.SCROLL_STATE_IDLE -> {
                            isUserInteracting = false
                            startAutoScroll()
                        }
                    }
                }
                
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageIndicators(position)
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.apartments.observe(viewLifecycleOwner) { apartments ->
            apartmentAdapter.submitList(apartments)
        }

        // Observe recent notifications for ViewPager2
        viewModel.recentNotifications.observe(viewLifecycleOwner) { notifications ->
            notificationViewPagerAdapter.submitList(notifications)
            setupPageIndicators(notifications.size)
            if (notifications.isNotEmpty()) {
                startAutoScroll()
            }
        }

        viewModel.amenities.observe(viewLifecycleOwner) { amenities ->
            amenityAdapter.submitList(amenities)
        }



        // Observe chart data
        viewModel.groupedInvoices.observe(viewLifecycleOwner) { groupedInvoices ->
            viewModel.apartmentNames.value?.let { apartmentNames ->
                invoiceChartPagerAdapter.submitData(groupedInvoices, apartmentNames)
                setupChartPageIndicators(apartmentNames.size)
                updateChartVisibility(apartmentNames.isNotEmpty())
                updateCurrentApartmentDisplay()
            }
        }

        viewModel.apartmentNames.observe(viewLifecycleOwner) { apartmentNames ->
            viewModel.groupedInvoices.value?.let { groupedInvoices ->
                invoiceChartPagerAdapter.submitData(groupedInvoices, apartmentNames)
                setupChartPageIndicators(apartmentNames.size)
                updateChartVisibility(apartmentNames.isNotEmpty())
                updateCurrentApartmentDisplay()
            }
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

    private fun startAutoScroll() {
        if (isUserInteracting) return
        
        stopAutoScroll()
        autoScrollRunnable = Runnable {
            if (!isUserInteracting && isAdded) {
                val adapter = notificationViewPagerAdapter
                val itemCount = adapter.itemCount
                if (itemCount > 1) {
                    val currentItem = binding.notificationsViewPager.currentItem
                    val nextItem = if (currentItem == itemCount - 1) 0 else currentItem + 1
                    binding.notificationsViewPager.setCurrentItem(nextItem, true)
                }
                startAutoScroll() // Schedule next scroll
            }
        }
        autoScrollRunnable?.let {
            autoScrollHandler.postDelayed(it, autoScrollDelay)
        }
    }

    private fun stopAutoScroll() {
        autoScrollRunnable?.let {
            autoScrollHandler.removeCallbacks(it)
        }
        autoScrollRunnable = null
    }

    private fun setupInvoiceChartsViewPager() {
        binding.invoiceChartsViewPager.apply {
            // Enable smooth scrolling
            offscreenPageLimit = 1
            
            // Add page transformer for smooth transitions
            setPageTransformer { page, position ->
                page.apply {
                    when {
                        position < -1 -> alpha = 0f
                        position <= 1 -> {
                            alpha = 1f
                            translationX = 0f
                            scaleX = 1f
                            scaleY = 1f
                        }
                        else -> alpha = 0f
                    }
                }
            }
            
            // Register page change callback
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateChartPageIndicators(position)
                    updateCurrentApartmentDisplay()
                }
            })
        }
    }

    private fun updateChartVisibility(hasData: Boolean) {
        if (hasData) {
            binding.invoiceChartsViewPager.visibility = View.VISIBLE
            binding.chartPageIndicatorContainer.visibility = View.VISIBLE
            binding.noBillsText.visibility = View.GONE
        } else {
            binding.invoiceChartsViewPager.visibility = View.GONE
            binding.chartPageIndicatorContainer.visibility = View.GONE
            binding.noBillsText.visibility = View.VISIBLE
        }
    }

    private fun updateCurrentApartmentDisplay() {
        val apartmentNames = viewModel.apartmentNames.value
        if (apartmentNames != null && apartmentNames.isNotEmpty()) {
            val currentPosition = binding.invoiceChartsViewPager.currentItem
            val displayText = "${currentPosition + 1}/${apartmentNames.size}"
            binding.tvCurrentApartment.text = displayText
            binding.tvCurrentApartment.visibility = View.VISIBLE
        } else {
            binding.tvCurrentApartment.visibility = View.GONE
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

    override fun onResume() {
        super.onResume()
        if (notificationViewPagerAdapter.itemCount > 1) {
            startAutoScroll()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
        _binding = null
    }

    private fun setupPageIndicators(count: Int) {
        pageIndicators.clear()
        binding.pageIndicatorContainer.removeAllViews()
        
        if (count <= 1) {
            binding.pageIndicatorContainer.visibility = View.GONE
            return
        }
        
        binding.pageIndicatorContainer.visibility = View.VISIBLE
        
        for (i in 0 until count) {
            val indicator = ImageView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0)
            indicator.layoutParams = layoutParams
            
            indicator.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (i == 0) R.drawable.page_indicator_active else R.drawable.page_indicator_inactive
                )
            )
            
            pageIndicators.add(indicator)
            binding.pageIndicatorContainer.addView(indicator)
        }
    }
    
    private fun updatePageIndicators(position: Int) {
        pageIndicators.forEachIndexed { index, indicator ->
            indicator.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (index == position) R.drawable.page_indicator_active else R.drawable.page_indicator_inactive
                )
            )
        }
    }

    private fun setupChartPageIndicators(count: Int) {
        chartPageIndicators.clear()
        binding.chartPageIndicatorContainer.removeAllViews()
        
        if (count <= 1) {
            binding.chartPageIndicatorContainer.visibility = View.GONE
            return
        }
        
        binding.chartPageIndicatorContainer.visibility = View.VISIBLE
        
        for (i in 0 until count) {
            val indicator = ImageView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0)
            indicator.layoutParams = layoutParams
            
            indicator.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (i == 0) R.drawable.page_indicator_active else R.drawable.page_indicator_inactive
                )
            )
            
            chartPageIndicators.add(indicator)
            binding.chartPageIndicatorContainer.addView(indicator)
        }
    }
    
    private fun updateChartPageIndicators(position: Int) {
        chartPageIndicators.forEachIndexed { index, indicator ->
            indicator.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (index == position) R.drawable.page_indicator_active else R.drawable.page_indicator_inactive
                )
            )
        }
    }
} 