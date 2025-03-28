package com.mit.apartmentmanagement.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.data.repository.AuthRepositoryImpl
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<TokenResponse>>()
    val loginResult: LiveData<Result<TokenResponse>> get() = _loginResult
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = repository.login(request)
        }
    }
}
