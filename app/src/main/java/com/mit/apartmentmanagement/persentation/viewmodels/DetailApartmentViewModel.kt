package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.model.Apartment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailApartmentViewModel @Inject constructor(): ViewModel(){

    private val _apartment = MutableStateFlow<Apartment?>(null)
    val apartment: StateFlow<Apartment?> = _apartment.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setApartment(apartment: Apartment) {
        viewModelScope.launch {
            _apartment.value = apartment
        }
    }

    fun clearError() {
        _error.value = null
    }
}