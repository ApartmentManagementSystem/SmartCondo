package com.mit.apartmentmanagement.data.apiservice

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/users/token-valid")
    suspend fun checkLoggedIn(): Response<Unit>

    @POST("api/users/logout")
    suspend fun logout(): Response<Unit>

}