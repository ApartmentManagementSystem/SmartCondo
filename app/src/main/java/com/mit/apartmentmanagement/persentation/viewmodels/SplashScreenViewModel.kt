package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.usecase.auth.CheckLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkLoggedInUseCase: CheckLoggedInUseCase,
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun checkLoggedIn() {
        viewModelScope.launch {
            val result = checkLoggedInUseCase()
            if (result.isSuccess) {
                _isLoggedIn.value = true
            } else {
                _isLoggedIn.value = false
            }
        }
    }

}