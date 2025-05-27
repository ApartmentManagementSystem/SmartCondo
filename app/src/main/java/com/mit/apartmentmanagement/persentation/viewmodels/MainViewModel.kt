package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.usecase.auth.LogoutUseCase
import com.mit.apartmentmanagement.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _logoutResult = MutableLiveData<NetworkResult<Unit>>()
    val logoutResult: LiveData<NetworkResult<Unit>> get() = _logoutResult

    fun logout() {
        _logoutResult.value = NetworkResult.Loading()
        viewModelScope.launch {
            val result = logoutUseCase()
            if (result.isSuccess) {
                _logoutResult.value = NetworkResult.Success(Unit)
            } else {
                _logoutResult.value = NetworkResult.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}