package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.data.model.owner.Owner
import com.mit.apartmentmanagement.domain.usecase.owner.OwnerUseCase
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerViewModel @Inject constructor(
    private val ownerUseCase: OwnerUseCase
) : ViewModel() {

    private val _owner = MutableLiveData<NetworkResult<Owner>>()
    val owner: LiveData<NetworkResult<Owner>> get() = _owner

    fun fetchOwner() {
        _owner.value = NetworkResult.Loading()

        viewModelScope.launch {
            val result = ownerUseCase()
            result
                .onSuccess { ownerData ->
                    _owner.value = NetworkResult.Success(ownerData)
                }
                .onFailure { exception ->
                    _owner.value = NetworkResult.Error(exception.message ?: "Unknown error")
                }
        }
    }
}
