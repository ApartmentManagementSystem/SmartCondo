package com.mit.apartmentmanagement.di

import android.content.Context
import com.mit.apartmentmanagement.data.apiservice.auth.ApartmentApi
import com.mit.apartmentmanagement.data.apiservice.auth.AuthApi
import com.mit.apartmentmanagement.data.apiservice.auth.InvoiceApi
import com.mit.apartmentmanagement.data.apiservice.noauth.NoAuthApi
import com.mit.apartmentmanagement.data.apiservice.auth.OwnerApi
import com.mit.apartmentmanagement.data.network.interceptors.NetworkManager
import com.mit.apartmentmanagement.data.apiservice.noauth.RefreshApi
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
    fun provideNoAuthOkHttpClient(
        networkManager: NetworkManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(networkManager)
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
    @Named("NoAuthRetrofit")
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
        @Named("NoAuthRetrofit") retrofit: Retrofit
    ): RefreshApi {
        return retrofit.create(RefreshApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("AuthRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNoAuthApi(@Named("NoAuthRetrofit") retrofit: Retrofit): NoAuthApi {
        return retrofit.create(NoAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApartmentApi(@Named("AuthRetrofit") retrofit: Retrofit): ApartmentApi {
        return retrofit.create(ApartmentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOwnerApi(@Named("AuthRetrofit") retrofit: Retrofit): OwnerApi {
        return retrofit.create(OwnerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInvoiceApi(@Named("AuthRetrofit") retrofit: Retrofit): InvoiceApi {
        return retrofit.create(InvoiceApi::class.java)
    }


}