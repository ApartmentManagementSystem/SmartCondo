package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.data.model.apartment.Apartment
import retrofit2.Response

interface ApartmentRepository {
    suspend fun getApartments(): Response<List<Apartment>>
}