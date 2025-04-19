package com.mit.apartmentmanagement.di

import android.app.Application
import android.content.Context
import com.mit.apartmentmanagement.data.network.util.NetworkStatus
import com.mit.apartmentmanagement.data.network.util.NetworkStatusImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: Application): Context = application
}