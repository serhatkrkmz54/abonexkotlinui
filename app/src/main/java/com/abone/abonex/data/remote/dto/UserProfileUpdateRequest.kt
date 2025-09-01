package com.abone.abonex.data.remote.dto

data class UserProfileUpdateRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val dateOfBirth: String?,
    val gender: String?,
    val phoneNumber: String?,
    val profileImageUrl: String?
)