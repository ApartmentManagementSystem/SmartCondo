package com.mit.apartmentmanagement.data.repository

import com.mit.apartmentmanagement.data.datasource.ApartmentRemoteDataSource
import com.mit.apartmentmanagement.data.model.apartment.Apartment
import com.mit.apartmentmanagement.domain.repository.ApartmentRepository
import retrofit2.Response
import javax.inject.Inject

class ApartmentRepositoryImpl @Inject constructor(
    private val apartmentRemoteDataSource: ApartmentRemoteDataSource
) : ApartmentRepository {
    override suspend fun getApartments(): Response<List<Apartment>> {
        return apartmentRemoteDataSource.getApartments()
    }
}