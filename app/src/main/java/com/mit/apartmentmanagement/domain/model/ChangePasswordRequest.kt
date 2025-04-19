package com.mit.apartmentmanagement.domain.model

data class ChangePasswordRequest (
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)