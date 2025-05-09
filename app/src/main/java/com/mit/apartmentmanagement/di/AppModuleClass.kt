package com.mit.apartmentmanagement.di

import com.mit.apartmentmanagement.data.network.util.NetworkStatus
import com.mit.apartmentmanagement.data.network.util.NetworkStatusImpl
import com.mit.apartmentmanagement.data.repository.AuthRepositoryImpl
import com.mit.apartmentmanagement.data.repository.InvoiceRepositoryImpl
import com.mit.apartmentmanagement.data.repository.NotificationRepositoryImpl
import com.mit.apartmentmanagement.data.repository.OwnerRepositoryImpl
import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import com.mit.apartmentmanagement.domain.repository.OwnerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Thay thế @Provides bằng @Binds trong abstract module
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleClass {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(repo: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(repo: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindOwnerRepository(repo: OwnerRepositoryImpl): OwnerRepository

    @Binds
    @Singleton
    abstract fun bindInvoiceRepository(repo: InvoiceRepositoryImpl): InvoiceRepository


    @Binds
    @Singleton
    abstract fun bindNetworkStatus(impl: NetworkStatusImpl): NetworkStatus
}