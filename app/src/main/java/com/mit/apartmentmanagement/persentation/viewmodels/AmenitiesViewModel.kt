package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.model.Amenity
import com.mit.apartmentmanagement.domain.model.AmenityType
import com.mit.apartmentmanagement.domain.usecase.amenities.GetAllAmenitiesUseCase
import com.mit.apartmentmanagement.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmenitiesViewModel @Inject constructor(
    private val getAllAmenitiesUseCase: GetAllAmenitiesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "AmenitiesViewModel"
    }

    private val _allAmenities = MutableStateFlow<List<Amenity>>(emptyList())
    val allAmenities: StateFlow<List<Amenity>> = _allAmenities.asStateFlow()

    private val _groupedAmenities = MutableStateFlow<Map<AmenityType, List<Amenity>>>(emptyMap())
    val groupedAmenities: StateFlow<Map<AmenityType, List<Amenity>>> = _groupedAmenities.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        Log.d(TAG, "AmenitiesViewModel initialized")
        loadAmenities()
    }

    fun loadAmenities() {
        Log.d(TAG, "Loading amenities")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getAllAmenitiesUseCase.getAllAmenities().collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _allAmenities.value = result.data
                            groupAmenitiesByType(result.data)
                            _isLoading.value = false
                            Log.d(TAG, "Loaded ${result.data.size} amenities")
                        }
                        is Result.Error -> {
                            _error.value = result.message
                            _isLoading.value = false
                            Log.e(TAG, "Error loading amenities: ${result.message}")
                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
                Log.e(TAG, "Exception loading amenities", e)
            }
        }
    }

    private fun groupAmenitiesByType(amenities: List<Amenity>) {
        val grouped = amenities.groupBy { it.type }
        _groupedAmenities.value = grouped
        Log.d(TAG, "Grouped amenities: ${grouped.keys}")
    }

    fun getAmenitiesByType(type: AmenityType): List<Amenity> {
        return _groupedAmenities.value[type] ?: emptyList()
    }

    fun getAmenityTypesWithData(): List<AmenityType> {
        return _groupedAmenities.value.keys.toList().sortedBy { it.ordinal }
    }

    fun refreshAmenities() {
        Log.d(TAG, "Refreshing amenities")
        loadAmenities()
    }

    fun clearError() {
        _error.value = null
    }

    fun getTypeDisplayName(type: AmenityType): String {
        return when (type) {
            AmenityType.SPORT -> "Sports & Fitness"
            AmenityType.ENTERTAINMENT -> "Entertainment"
            AmenityType.RELAXATION -> "Relaxation & Wellness"
            AmenityType.COMMUNITY -> "Community Spaces"
            AmenityType.OTHER -> "Other Amenities"
        }
    }

    fun getTypeDescription(type: AmenityType): String {
        return when (type) {
            AmenityType.SPORT -> "Gym, pool, courts, and fitness facilities"
            AmenityType.ENTERTAINMENT -> "Game rooms, theaters, and entertainment areas"
            AmenityType.RELAXATION -> "Spa, sauna, gardens, and relaxation zones"
            AmenityType.COMMUNITY -> "Meeting rooms, lounges, and social spaces"
            AmenityType.OTHER -> "Additional facilities and services"
        }
    }
} 