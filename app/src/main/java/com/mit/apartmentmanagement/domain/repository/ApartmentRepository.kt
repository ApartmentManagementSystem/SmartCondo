package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.domain.model.Apartment
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ApartmentRepository {
    suspend fun getApartments(): Flow<Result<List<Apartment>>>
}