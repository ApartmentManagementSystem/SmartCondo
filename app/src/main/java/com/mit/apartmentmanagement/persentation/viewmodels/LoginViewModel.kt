package com.mit.apartmentmanagement.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.usecase.auth.LoginUseCase
import com.mit.apartmentmanagement.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<NetworkResult<Unit>>()
    val loginResult: LiveData<NetworkResult<Unit>> get() = _loginResult

    fun login(loginRequest: LoginRequest) {
        _loginResult.value = NetworkResult.Loading()
        viewModelScope.launch {
            val result = loginUseCase(loginRequest)
            if (result.isSuccess) {
                _loginResult.value = NetworkResult.Success(Unit)
            } else {

                _loginResult.value = NetworkResult.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}