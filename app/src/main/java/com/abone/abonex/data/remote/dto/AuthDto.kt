package com.abone.abonex.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val dateOfBirth: String,
    val gender: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null
)

data class ReactivateRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String
)

data class ApiErrorResponse(
    val error: String
)