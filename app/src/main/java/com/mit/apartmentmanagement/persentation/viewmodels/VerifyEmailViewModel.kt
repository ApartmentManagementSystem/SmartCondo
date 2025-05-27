package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.usecase.auth.CheckEmailRegisteredUseCase
import com.mit.apartmentmanagement.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val checkEmailRegisteredUseCase: CheckEmailRegisteredUseCase,
) : ViewModel() {

    private var _isRegistered = MutableLiveData<NetworkResult<Unit>>()
    val isRegistered: LiveData<NetworkResult<Unit>> get() = _isRegistered

    fun checkEmailRegistered(email: String) {
        _isRegistered.value = NetworkResult.Loading()
        viewModelScope.launch {
            val result = checkEmailRegisteredUseCase(email)
            if (result.isSuccess) {
                _isRegistered.postValue(NetworkResult.Success(Unit))
            } else {
                _isRegistered.postValue(
                    NetworkResult.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                )
            }
        }
    }


}