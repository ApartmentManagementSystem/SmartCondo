package com.mit.apartmentmanagement.domain.usecase.apartment

import com.mit.apartmentmanagement.domain.repository.ApartmentRepository
import javax.inject.Inject

class getListApartmentsUseCase @Inject constructor(
    private val apartmentRepository: ApartmentRepository
) {
    suspend operator fun invoke() = apartmentRepository.getApartments()
}