package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.apartmentmanagement.domain.util.Result
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mit.apartmentmanagement.domain.model.Amenity
import com.mit.apartmentmanagement.domain.model.Apartment
import com.mit.apartmentmanagement.domain.model.Notification
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.usecase.amenities.GetAllAmenitiesUseCase
import com.mit.apartmentmanagement.domain.usecase.apartment.GetApartmentsUseCase
import com.mit.apartmentmanagement.domain.usecase.invoice.GetSixInvoiceMonthlyUseCase
import com.mit.apartmentmanagement.domain.usecase.invoice.GetInvoiceForChartUseCase
import com.mit.apartmentmanagement.domain.usecase.notification.GetFiveNotificationUseCase
import com.mit.apartmentmanagement.domain.usecase.notification.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getApartmentUseCase: GetApartmentsUseCase,
    private val getSixInvoiceMonthlyUseCase: GetSixInvoiceMonthlyUseCase,
    private val getAllAmenitiesUseCase: GetAllAmenitiesUseCase,
    private val getFiveNotificationUseCase: GetFiveNotificationUseCase,
    private val getInvoiceForChartUseCase: GetInvoiceForChartUseCase
) : ViewModel() {

    private val _apartments = MutableLiveData<List<Apartment>>()
    val apartments: LiveData<List<Apartment>> = _apartments

    // Add recent notifications for ViewPager2 - using GetFiveNotificationUseCase
    private val _recentNotifications = MutableLiveData<List<Notification>>()
    val recentNotifications: LiveData<List<Notification>> = _recentNotifications

    private val _invoices = MutableLiveData<List<InvoiceMonthly>>()
    val invoices: LiveData<List<InvoiceMonthly>> = _invoices



    // Grouped invoices by apartment name for ViewPager2
    private val _groupedInvoices = MutableLiveData<Map<String, List<InvoiceMonthly>>>()
    val groupedInvoices: LiveData<Map<String, List<InvoiceMonthly>>> = _groupedInvoices

    // List of apartment names for ViewPager2 tabs
    private val _apartmentNames = MutableLiveData<List<String>>()
    val apartmentNames: LiveData<List<String>> = _apartmentNames

    private val _greeting = MutableLiveData<String>()
    val greeting: LiveData<String> = _greeting

    private val _amenities = MutableLiveData<List<Amenity>>()
    val amenities: LiveData<List<Amenity>> = _amenities

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        updateGreeting()
        loadData()
    }

    private fun updateGreeting() {
        val currentTime = LocalTime.now()
        _greeting.value = when {
            currentTime.hour in 5..11 -> "Good Morning"
            currentTime.hour in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getApartmentUseCase().collect { result ->
                    when(result) {
                        is Result.Success -> _apartments.value = result.data
                        is Result.Error -> _error.value = result.message
                        is Result.Loading -> _isLoading.value = true
                    }
                }

                getFiveNotificationUseCase().collect { result ->
                    when(result) {
                        is Result.Success -> _recentNotifications.value = result.data
                        is Result.Error -> _error.value = result.message
                        is Result.Loading -> _isLoading.value = true
                    }
                    Log.d("HomeViewModel", "Loaded recent notifications: ${_recentNotifications.value}")
                }
                // Load chart invoices
                getInvoiceForChartUseCase().collect { result ->
                    when(result) {
                        is Result.Success -> {
                            processChartInvoices(result.data)
                        }
                        is Result.Error -> _error.value = result.message
                        is Result.Loading -> _isLoading.value = true
                    }
                }
                getAllAmenitiesUseCase.getAllAmenities().collect { result ->
                    when (result) {
                        is Result.Success -> _amenities.value = result.data
                        is Result.Error -> _error.value = result.message
                        is Result.Loading -> _isLoading.value = true
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshData() {
        loadData()
    }

    private fun processChartInvoices(invoices: List<InvoiceMonthly>) {
        // Group invoices by apartment name
        val grouped = invoices.groupBy { it.apartmentName }
        _groupedInvoices.value = grouped
        
        // Extract apartment names
        val apartmentNames = grouped.keys.toList().sorted()
        _apartmentNames.value = apartmentNames
        
        Log.d("HomeViewModel", "Processed ${invoices.size} invoices into ${apartmentNames.size} apartments")
        Log.d("HomeViewModel", "Apartment names: $apartmentNames")
    }

    fun getInvoicesForApartment(apartmentName: String): List<InvoiceMonthly> {
        return _groupedInvoices.value?.get(apartmentName) ?: emptyList()
    }
} 