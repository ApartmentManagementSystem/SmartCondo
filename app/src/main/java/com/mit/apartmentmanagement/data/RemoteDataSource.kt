package com.mit.apartmentmanagement.data

import com.mit.apartmentmanagement.data.network.apiservers.AuthApi
import com.mit.apartmentmanagement.data.network.apiservers.RefreshApi
import com.mit.apartmentmanagement.models.ApiResponse
import retrofit2.Response

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val refreshApi: RefreshApi
) {

    suspend fun loginCheck(token:String): Response<ApiResponse<Boolean>> {
        return authApi.checkLogin(token)
    }

}