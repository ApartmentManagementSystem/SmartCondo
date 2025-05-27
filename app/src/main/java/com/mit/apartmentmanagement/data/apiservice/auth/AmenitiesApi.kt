package com.mit.apartmentmanagement.data.apiservice.auth

import com.mit.apartmentmanagement.domain.model.Amenity
import retrofit2.Response
import retrofit2.http.GET

interface AmenitiesApi {
    @GET("api/amenities")
    suspend fun getAmenities(): Response<List<Amenity>>
}