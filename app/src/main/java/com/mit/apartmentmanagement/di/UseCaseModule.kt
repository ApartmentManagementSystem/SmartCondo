package com.mit.apartmentmanagement.di

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import com.mit.apartmentmanagement.domain.usecase.auth.ChangePasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.ForgetPasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.LoginUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.LogoutUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.RecoveryPasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.notification.GetNotificationApiUseCase
import com.mit.apartmentmanagement.domain.usecase.notification.ObserveStompNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideForgetPasswordUseCase(authRepository: AuthRepository): ForgetPasswordUseCase {
        return ForgetPasswordUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRecoveryPasswordUseCase(authRepository: AuthRepository): RecoveryPasswordUseCase {
        return RecoveryPasswordUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideChangePasswordUseCase(authRepository: AuthRepository): ChangePasswordUseCase {
        return ChangePasswordUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetNotificationApiUseCase(notificationRepository: NotificationRepository): GetNotificationApiUseCase {
        return GetNotificationApiUseCase(notificationRepository)
    }

    @Provides
    @Singleton
    fun provideObserveStompNotificationUseCase(notificationRepository: NotificationRepository): ObserveStompNotificationUseCase {
        return ObserveStompNotificationUseCase(notificationRepository)
    }




}