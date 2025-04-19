package com.mit.apartmentmanagement.data.apiservers

import com.mit.apartmentmanagement.data.model.apartment.Apartment
import retrofit2.Response
import retrofit2.http.GET

interface ApartmentApi {
    @GET("api/apartments")
    suspend fun getApartments(): Response<List<Apartment>>
}