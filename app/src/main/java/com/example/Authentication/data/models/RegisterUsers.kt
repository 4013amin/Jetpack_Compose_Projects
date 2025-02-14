package com.example.Authentication.data.models

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val credit: Int
)

data class RegisterResponse(
    val success: String?,
    val error: String?
)

data class ProfileResponse(
    val credit: Int,
    val image: String
)

