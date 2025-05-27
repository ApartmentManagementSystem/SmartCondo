package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.usecase.invoice.GetInvoiceDetailUseCase
import com.mit.apartmentmanagement.domain.usecase.invoice.PayInvoiceMonthlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMonthlyViewModel @Inject constructor(
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
    private val payInvoiceMonthlyUseCase: PayInvoiceMonthlyUseCase
) : ViewModel() {

    private val _invoiceDetails = MutableStateFlow<InvoiceMonthly?>(null)
    val invoiceDetails: StateFlow<InvoiceMonthly?> = _invoiceDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isPaymentLoading = MutableStateFlow(false)
    val isPaymentLoading: StateFlow<Boolean> = _isPaymentLoading.asStateFlow()

    private val _paymentSuccess = MutableStateFlow<String?>(null)
    val paymentSuccess: StateFlow<String?> = _paymentSuccess.asStateFlow()

    private val _paymentError = MutableStateFlow<String?>(null)
    val paymentError: StateFlow<String?> = _paymentError.asStateFlow()

    fun getInvoiceDetails(invoiceId: String) {
        viewModelScope.launch {
            try {
                getInvoiceDetailUseCase(invoiceId).collect { result ->
                    when (result) {
                        is com.mit.apartmentmanagement.domain.util.Result.Success -> {
                            _isLoading.value = false
                            _invoiceDetails.value = result.data
                            Log.d("DetailMonthlyViewModel", "Invoice details: ${result.data}")
                            _error.value = null
                        }
                        is com.mit.apartmentmanagement.domain.util.Result.Error -> {
                            _isLoading.value = false
                            _error.value = result.exception?.message ?: "Unknown error occurred"
                        }
                        is com.mit.apartmentmanagement.domain.util.Result.Loading -> {
                            _isLoading.value = true
                            _error.value = null
                        }
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "An unexpected error occurred"
            }
        }
    }

    fun payInvoice(invoiceId: String) {
        viewModelScope.launch {
            try {
                _isPaymentLoading.value = true
                _paymentError.value = null
                _paymentSuccess.value = null
                
                payInvoiceMonthlyUseCase(invoiceId).collect { result ->
                    when (result) {
                        is com.mit.apartmentmanagement.domain.util.Result.Success -> {
                            _isPaymentLoading.value = false
                            _invoiceDetails.value = result.data
                            _paymentSuccess.value = "Thanh toán thành công!"
                            Log.d("DetailMonthlyViewModel", "Payment successful: ${result.data}")
                        }
                        is com.mit.apartmentmanagement.domain.util.Result.Error -> {
                            _isPaymentLoading.value = false
                            _paymentError.value = result.exception?.message ?: "Thanh toán thất bại"
                            Log.e("DetailMonthlyViewModel", "Payment error: ${result.exception?.message}")
                        }
                        is com.mit.apartmentmanagement.domain.util.Result.Loading -> {
                            _isPaymentLoading.value = true
                        }
                    }
                }
            } catch (e: Exception) {
                _isPaymentLoading.value = false
                _paymentError.value = e.message ?: "Đã xảy ra lỗi khi thanh toán"
                Log.e("DetailMonthlyViewModel", "Payment exception: ${e.message}", e)
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearPaymentMessages() {
        _paymentSuccess.value = null
        _paymentError.value = null
    }

    fun retry(invoiceId: String) {
        getInvoiceDetails(invoiceId)
    }
} 