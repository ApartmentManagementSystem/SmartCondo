package com.mit.apartmentmanagement.di

import android.content.Context
import com.mit.apartmentmanagement.data.network.apiservers.AuthApi
import com.mit.apartmentmanagement.data.network.NetworkManager
import com.mit.apartmentmanagement.data.network.apiservers.RefreshApi
import com.mit.apartmentmanagement.data.network.TokenManager
import com.mit.apartmentmanagement.data.network.interceptors.AuthInterceptor
import com.mit.apartmentmanagement.data.network.interceptors.RefreshTokenInterceptor
import com.mit.apartmentmanagement.util.Constant.BASE_URL
import com.mit.apartmentmanagement.util.Constant.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager {
        return NetworkManager(context)
    }

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideRefreshInterceptor(
        tokenManager: TokenManager,
        refreshApi: RefreshApi
    ): RefreshTokenInterceptor {
        return RefreshTokenInterceptor(tokenManager, refreshApi)
    }

    // Client đơn giản KHÔNG có interceptor
    @Provides
    @Singleton
    @Named("NoAuthClient")
    fun provideNoAuthOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    // Client chính có đầy đủ interceptor
    @Provides
    @Singleton
    @Named("AuthClient")
    fun provideAuthOkHttpClient(
        networkManager: NetworkManager,
        authInterceptor: AuthInterceptor,
        refreshInterceptor: RefreshTokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkManager)
            .addInterceptor(authInterceptor)
            .addInterceptor(refreshInterceptor)
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @Named("RefreshRetrofit")
    fun provideRefreshRetrofit(
        @Named("NoAuthClient") client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideRetrofit(
        @Named("AuthClient") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideRefreshApi(
        @Named("RefreshRetrofit") retrofit: Retrofit
    ): RefreshApi {
        return retrofit.create(RefreshApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("AuthRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }



}