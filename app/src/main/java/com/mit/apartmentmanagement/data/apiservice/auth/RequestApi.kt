package com.mit.apartmentmanagement.data.apiservice.auth

import com.mit.apartmentmanagement.data.model.PageResponse
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import retrofit2.Response
import retrofit2.http.*

interface RequestApi {

    @POST("api/requests")
    suspend fun postRequest(): Response<Unit>

    @GET("api/requests/my-requests")
    suspend fun getRequests(
        @Query("page") page: Int = 0,
        @Query("limit") size: Int = 10
    ): Response<PageResponse<Request>>

    @GET("api/requests/my-requests-by-status")
    suspend fun getRequestsByStatus(
        @Query("status") status: RequestStatus,
        @Query("page") page: Int = 0,
        @Query("limit") size: Int = 10
    ): Response<PageResponse<Request>>

    @GET("api/requests/my-request")
    suspend fun searchRequests(
        @Query("query") query: String,
        @Query("page") page: Int = 0,
        @Query("limit") size: Int = 10
    ): Response<PageResponse<Request>>

}