package com.mit.apartmentmanagement.data.repository

import com.mit.apartmentmanagement.data.datasource.OwnerRemoteDataSource
import com.mit.apartmentmanagement.data.model.owner.Owner
import com.mit.apartmentmanagement.data.network.interceptors.NetworkManager
import com.mit.apartmentmanagement.data.network.util.NetworkStatus
import com.mit.apartmentmanagement.domain.repository.OwnerRepository
import javax.inject.Inject

class OwnerRepositoryImpl @Inject constructor(
    private val remoteDataSource: OwnerRemoteDataSource,
) : OwnerRepository {

    override suspend fun getOwner(): Result<Owner> {
        return try {
            val response = remoteDataSource.getOwners()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("Request failed: ${response.message()}"))
            }
        } catch (e: NetworkManager.NoNetworkException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.localizedMessage}"))
        }
    }
}
