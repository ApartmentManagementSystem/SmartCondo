package com.mit.apartmentmanagement.domain.model

data class LoginResponse(
    val status:Int, // 1 is success and 0 is failure
    val message:String,
    val token: String,
)