package com.mit.apartmentmanagement.data.repository

import com.mit.apartmentmanagement.data.apiservice.auth.ApartmentApi
import com.mit.apartmentmanagement.data.util.safeApiCall
import com.mit.apartmentmanagement.domain.model.Apartment
import com.mit.apartmentmanagement.domain.repository.ApartmentRepository
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApartmentRepositoryImpl @Inject constructor(
    private val api: ApartmentApi
) : ApartmentRepository {
    override suspend fun getApartments(): Flow<Result<List<Apartment>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.getApartments()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }
}