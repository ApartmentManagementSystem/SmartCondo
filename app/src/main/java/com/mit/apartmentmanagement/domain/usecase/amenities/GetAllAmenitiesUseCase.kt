package com.mit.apartmentmanagement.domain.usecase.amenities

import com.mit.apartmentmanagement.domain.repository.AmenitiesRepository
import javax.inject.Inject

class GetAllAmenitiesUseCase @Inject constructor(
    private val amenitiesRepository: AmenitiesRepository
) {

    suspend fun getAllAmenities() = amenitiesRepository.getAmenities()
}