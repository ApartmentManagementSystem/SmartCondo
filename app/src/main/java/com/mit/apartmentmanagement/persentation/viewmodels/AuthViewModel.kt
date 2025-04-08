package com.mit.apartmentmanagement.persentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.usecase.auth.ChangePasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.ForgetPasswordUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.LoginUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.LogoutUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.RecoveryPasswordUseCase
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val recoveryPasswordUseCase: RecoveryPasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val application: Application

) : AndroidViewModel(application) {
    private val _loginResult = MutableLiveData<Result<TokenResponse>>()
    val loginResult: LiveData<Result<TokenResponse>> get() = _loginResult
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = loginUseCase(request) ?: Result.failure(Exception("Unexpected null result"))
        }
    }

    private val _logoutResult = MutableLiveData<Result<Unit>>()
    val logoutResult: LiveData<Result<Unit>> get() = _logoutResult

    fun logout() {
        viewModelScope.launch {
            _logoutResult.value = logoutUseCase() ?: Result.failure(Exception("Unexpected null result"))
        }
    }

    private val _forgetPasswordResult = MutableLiveData<Result<Unit>>()
    val forgetPasswordResult: LiveData<Result<Unit>> get() = _forgetPasswordResult

    fun forgotPassword(email: String)  {
        viewModelScope.launch {
            _forgetPasswordResult.value = forgetPasswordUseCase(email) ?: Result.failure(Exception("Unexpected null result"))
        }
    }

    private val _recoveryPasswordResult = MutableLiveData<Result<Unit>>()
    val recoveryPasswordResult: LiveData<Result<Unit>> get() = _recoveryPasswordResult

    fun recoveryPassword(code: String, newPassword: String, confirmPassword: String)  {
        viewModelScope.launch {
            _recoveryPasswordResult.value = recoveryPasswordUseCase(code, newPassword, confirmPassword) ?: Result.failure(Exception("Unexpected null result"))
        }
    }
    private val _changePasswordResult = MutableLiveData<Result<Unit>>()
    val changePasswordResult: LiveData<Result<Unit>> get() = _changePasswordResult

    fun changePassword(request: ChangePasswordRequest)  {
        viewModelScope.launch {
            _changePasswordResult.value = changePasswordUseCase(request) ?: Result.failure(Exception("Unexpected null result"))
        }
    }


}