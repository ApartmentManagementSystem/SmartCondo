package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.data.model.owner.Owner


interface OwnerRepository {
    suspend fun getOwner(): Result<Owner>
}