package com.mit.apartmentmanagement.di

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.domain.usecase.ChangePasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.ForgetPasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.LoginUseCase
import com.mit.apartmentmanagement.domain.usecase.LogoutUseCase
import com.mit.apartmentmanagement.domain.usecase.RecoveryPasswordUseCase
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

}