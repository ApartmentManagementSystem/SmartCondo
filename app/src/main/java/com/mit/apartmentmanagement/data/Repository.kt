package com.mit.apartmentmanagement.data

import com.mit.apartmentmanagement.di.DataBaseModule
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    //localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    //val local = localDataSource
}