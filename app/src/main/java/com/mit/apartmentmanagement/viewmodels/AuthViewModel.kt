package com.mit.apartmentmanagement.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mit.apartmentmanagement.data.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource, application: Application
) : AndroidViewModel(application) {
}