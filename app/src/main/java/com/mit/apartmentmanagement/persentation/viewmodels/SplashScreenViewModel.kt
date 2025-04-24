package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.usecase.auth.CheckLoggedInUseCase
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkLoggedInUseCase: CheckLoggedInUseCase,
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<NetworkResult<Boolean> >()
    val isLoggedIn: LiveData<NetworkResult<Boolean>> get() = _isLoggedIn

    fun checkLoggedIn() {
        viewModelScope.launch {
            delay(2000)
            val result = checkLoggedInUseCase()
            Log.d("SplashScreenViewModel","checkLoggedIn() called")
            if(result.isSuccess){

                _isLoggedIn.value = NetworkResult.Success(true)
                Log.d("SplashScreenViewModel","_isLoggedIn.value = true")
            }else{
                _isLoggedIn.value = NetworkResult.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                Log.d("SplashScreenViewModel","${result.exceptionOrNull()}")
            }
        }
    }


}