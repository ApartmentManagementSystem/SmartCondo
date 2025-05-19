package com.mit.apartmentmanagement.data.datasource

import com.mit.apartmentmanagement.data.apiservice.auth.OwnerApi
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import javax.inject.Inject

class OwnerRemoteDataSource @Inject constructor(
    private val ownerApi: OwnerApi
) {
    suspend fun getOwners() = ownerApi.getOwners()
    suspend fun changePassword(request: ChangePasswordRequest) = ownerApi.changePassword(request)
}