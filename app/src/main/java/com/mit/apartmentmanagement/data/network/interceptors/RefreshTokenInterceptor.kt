package com.mit.apartmentmanagement.data.network.interceptors

import com.mit.apartmentmanagement.data.apiservice.RefreshApi
import com.mit.apartmentmanagement.data.network.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshApi: RefreshApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Thực hiện request ban đầu
        val originalResponse = chain.proceed(chain.request())

        // Kiểm tra lỗi 401 (Unauthorized)
        if (originalResponse.code != 401) {
            return originalResponse
        }

        synchronized(this) {
            // Kiểm tra xem đã refresh token chưa để tránh lặp vô hạn
            if (originalResponse.request.header("Authorization")?.contains("Retry") == true) {
                return originalResponse
            }

            val refreshToken = tokenManager.getRefreshToken() ?: run {
                tokenManager.clearTokens()
                return originalResponse
            }

            // Thực hiện refresh token trên background thread
            val newTokenResponse = runBlocking(Dispatchers.IO) {
                refreshApi.refreshToken(refreshToken)
            }

            if (!newTokenResponse.isSuccessful || newTokenResponse.body() == null) {
                tokenManager.clearTokens()
                return originalResponse
            }

            // Lưu token mới
            val tokenData = newTokenResponse.body()!!
            tokenManager.saveTokens(tokenData.accessToken, tokenData.refreshToken)

            // Retry request với token mới (thêm header "Retry" để đánh dấu)
            val newRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer ${tokenData.accessToken}")
                .header("Retry", "1") // Ngăn chặn lặp vô hạn
                .build()

            return chain.proceed(newRequest)
        }
    }
}