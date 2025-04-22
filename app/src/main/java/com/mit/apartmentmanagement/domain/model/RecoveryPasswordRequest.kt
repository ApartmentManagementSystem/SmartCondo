package com.mit.apartmentmanagement.domain.model

data class RecoveryPasswordRequest (
    val resetCode: String,
    val newPassword: String,
    val confirmPassword: String
)