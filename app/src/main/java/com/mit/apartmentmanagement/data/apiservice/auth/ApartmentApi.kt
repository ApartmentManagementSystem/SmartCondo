package com.mit.apartmentmanagement.data.apiservice.auth

import com.mit.apartmentmanagement.data.model.apartment.Apartment
import retrofit2.Response
import retrofit2.http.GET

interface ApartmentApi {
    @GET("api/apartments")
    suspend fun getApartments(): Response<List<Apartment>>
}