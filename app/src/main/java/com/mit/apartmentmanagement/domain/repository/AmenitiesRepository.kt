package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.domain.model.Amenity
import kotlinx.coroutines.flow.Flow
import com.mit.apartmentmanagement.domain.util.Result


interface AmenitiesRepository  {
    suspend fun getAmenities(): Flow<Result<List<Amenity>>>
}