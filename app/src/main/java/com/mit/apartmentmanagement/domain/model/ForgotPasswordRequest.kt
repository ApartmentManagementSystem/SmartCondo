package com.mit.apartmentmanagement.domain.model

data class ForgotPasswordRequest (
    val code : String,
    val newPassword : String,
    val confirmPassword : String

)