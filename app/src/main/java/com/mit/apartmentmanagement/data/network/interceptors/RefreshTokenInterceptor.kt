package com.mit.apartmentmanagement.data.network.interceptors

import com.mit.apartmentmanagement.data.network.AuthApi
import com.mit.apartmentmanagement.data.network.RefreshApi
import com.mit.apartmentmanagement.data.network.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshApi: RefreshApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) { // Token expired
            synchronized(this) {
                val refreshToken = tokenManager.getRefreshToken() ?: return response

                val newTokenResponse = runBlocking {
                    refreshApi.refreshToken(refreshToken)
                    //authApi.get().refreshToken(refreshToken)
                }

                if (newTokenResponse.isSuccessful) {
                    newTokenResponse.body()?.data?.let { tokenResponse ->
                        // Save new tokens
                        tokenManager.saveTokens(
                            tokenResponse.accessToken,
                            tokenResponse.refreshToken
                        )
                        // Retry original request with new token
                        val newRequest = chain.request().newBuilder()
                            .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                            .build()
                        return chain.proceed(newRequest)
                    }
                } else {
                    // Refresh token failed - logout user
                    tokenManager.clearTokens()

                }
            }
        }
        return response

    }
}