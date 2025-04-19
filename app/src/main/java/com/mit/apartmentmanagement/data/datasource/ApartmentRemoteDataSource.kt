package com.mit.apartmentmanagement.data.datasource

import com.mit.apartmentmanagement.data.apiservers.ApartmentApi
import javax.inject.Inject

class ApartmentRemoteDataSource @Inject constructor(
    private val apartmentApi: ApartmentApi
) {
    suspend fun getApartments() = apartmentApi.getApartments()
}