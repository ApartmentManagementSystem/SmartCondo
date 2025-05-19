package com.mit.apartmentmanagement.data.repository

import com.mit.apartmentmanagement.data.apiservice.auth.AmenitiesApi
import com.mit.apartmentmanagement.data.util.safeApiCall
import com.mit.apartmentmanagement.domain.model.Amenity
import com.mit.apartmentmanagement.domain.repository.AmenitiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.mit.apartmentmanagement.domain.util.Result
import javax.inject.Inject

class AmenitiesRepositoryImpl @Inject constructor(
    private val amenitiesApi: AmenitiesApi,
): AmenitiesRepository {
    override suspend fun getAmenities(): Flow<Result<List<Amenity>>> = flow {
        emit(Result.Loading)
        when (val result = safeApiCall { amenitiesApi.getAmenities() }) {
            is Result.Success -> {
                emit(Result.Success(result.data))
            }

            is Result.Error -> emit(result)
            is Result.Loading -> emit(result)
        }
    }

}