package com.mit.apartmentmanagement.data.apiservice

import retrofit2.Response
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/logged-in-check")
    suspend fun checkLoggedIn(): Response<Unit>

    @POST("api/users/logout")
    suspend fun logout(): Response<Unit>

}