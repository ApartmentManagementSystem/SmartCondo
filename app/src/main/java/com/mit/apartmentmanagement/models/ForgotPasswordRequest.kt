package com.mit.apartmentmanagement.models

data class ForgotPasswordRequest (
    val code : String,
    val newPassword : String,
    val confirmPassword : String

)