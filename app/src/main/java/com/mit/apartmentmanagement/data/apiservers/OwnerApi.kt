package com.mit.apartmentmanagement.data.apiservers

import com.mit.apartmentmanagement.data.model.owner.Owner
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface OwnerApi {
    @GET("api/owner")
    suspend fun getOwners(): Response<Owner>
}