package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.domain.usecase.auth.LoginUseCase
import com.mit.apartmentmanagement.domain.usecase.auth.RecoveryPasswordUseCase
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryPasswordViewModel @Inject constructor(
    private val recoveryPasswordUseCase: RecoveryPasswordUseCase
) : ViewModel() {
    private val _recoveryPasswordResult = MutableLiveData<NetworkResult<Unit>>()
    val recoveryPasswordResult: LiveData<NetworkResult<Unit>> get() = _recoveryPasswordResult

    fun recoveryPassword(request: RecoveryPasswordRequest) {
        _recoveryPasswordResult.value = NetworkResult.Loading()
        viewModelScope.launch {
            val result = recoveryPasswordUseCase(request)
            if (result.isSuccess) {
                _recoveryPasswordResult.value = NetworkResult.Success(Unit)
            } else {
                _recoveryPasswordResult.value =
                    NetworkResult.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                Log.d("RecoveryPasswordViewModel", result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}