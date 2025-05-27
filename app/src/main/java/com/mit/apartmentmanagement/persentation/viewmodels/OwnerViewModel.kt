package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.ViewModel
import com.mit.apartmentmanagement.domain.usecase.owner.OwnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnerViewModel @Inject constructor(
    private val ownerUseCase: OwnerUseCase,
    //private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

//    private val _owner = MutableLiveData<NetworkResult<Owner>>()
//    val owner: LiveData<NetworkResult<Owner>> get() = _owner
//
//    fun fetchOwner() {
//        _owner.value = NetworkResult.Loading()
//
//        viewModelScope.launch {
//            val result = ownerUseCase()
//            result
//                .onSuccess { ownerData ->
//                    _owner.value = NetworkResult.Success(ownerData)
//                }
//                .onFailure { exception ->
//                    _owner.value = NetworkResult.Error(exception.message ?: "Unknown error")
//                }
//        }
//    }
//
//    private val _changePasswordResult = MutableLiveData<NetworkResult<Unit>>()
//    val changePasswordResult: LiveData<NetworkResult<Unit>> get() = _changePasswordResult
//
//    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
//        _changePasswordResult.value = NetworkResult.Loading()
//        viewModelScope.launch {
//            val result = changePasswordUseCase(
//                ChangePasswordRequest(
//                    oldPassword,
//                    newPassword,
//                    confirmPassword
//                )
//            )
//            result
//                .onSuccess {
//                    _changePasswordResult.value = NetworkResult.Success(Unit)
//                }
//                .onFailure { exception ->
//                    _changePasswordResult.value =
//                        NetworkResult.Error(exception.message ?: "Unknown error")
//                }
//
//        }
//    }

}
