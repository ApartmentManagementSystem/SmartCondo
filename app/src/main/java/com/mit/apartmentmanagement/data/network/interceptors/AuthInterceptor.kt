package com.mit.apartmentmanagement.data.network.interceptors

import android.util.Log
import com.mit.apartmentmanagement.data.network.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .apply {
                tokenManager.getAccessToken()?.let { token ->
                    addHeader("Authorization", "Bearer $token")
                    Log.d("AuthInterceptor", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(request)
    }

}