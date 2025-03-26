package com.mit.apartmentmanagement.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.data.Repository
import com.mit.apartmentmanagement.data.network.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,private val tokenManager: TokenManager, application: Application
) : AndroidViewModel(application) {

    private val _isLogined = MutableLiveData<Boolean>()
    val isLogined: LiveData<Boolean> get() = _isLogined

    fun checkLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = tokenManager.getAccessToken()
            if (token.isNullOrEmpty()) {

                _isLogined.postValue(false)

            }else{
                _isLogined.postValue(true)

            }
        }
    }

}