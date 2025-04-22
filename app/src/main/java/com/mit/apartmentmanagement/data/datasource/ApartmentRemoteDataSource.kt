package com.mit.apartmentmanagement.data.datasource

import com.mit.apartmentmanagement.data.apiservice.ApartmentApi
import javax.inject.Inject

class ApartmentRemoteDataSource @Inject constructor(
    private val apartmentApi: ApartmentApi
) {
    suspend fun getApartments() = apartmentApi.getApartments()
}