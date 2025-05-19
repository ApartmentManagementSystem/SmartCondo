package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.domain.model.Owner


interface OwnerRepository {
    suspend fun getOwner(): Result<Owner>
}