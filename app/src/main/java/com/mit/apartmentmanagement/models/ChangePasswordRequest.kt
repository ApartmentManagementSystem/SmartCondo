package com.mit.apartmentmanagement.models

data class ChangePasswordRequest (
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)