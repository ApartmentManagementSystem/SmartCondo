package com.mit.apartmentmanagement.data.apiservice

import com.mit.apartmentmanagement.data.model.owner.Owner
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface OwnerApi {

    @GET("api/owner")
    suspend fun getOwners(): Response<Owner>

    @PUT("api/users/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<Unit>
}