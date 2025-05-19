package com.mit.apartmentmanagement.domain.usecase.owner

import com.mit.apartmentmanagement.domain.model.Owner
import com.mit.apartmentmanagement.domain.repository.OwnerRepository
import javax.inject.Inject

class OwnerUseCase @Inject constructor(
    private val ownerRepository: OwnerRepository,
    ) {
    suspend operator fun invoke(): Result<Owner> {
        return ownerRepository.getOwner()
    }
}