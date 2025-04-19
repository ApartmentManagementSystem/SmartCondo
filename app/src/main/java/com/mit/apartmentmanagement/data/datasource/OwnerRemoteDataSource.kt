package com.mit.apartmentmanagement.data.datasource

import com.mit.apartmentmanagement.data.apiservers.OwnerApi
import javax.inject.Inject

class OwnerRemoteDataSource @Inject constructor(
    private val ownerApi: OwnerApi
) {
    suspend fun getOwners() = ownerApi.getOwners()
}