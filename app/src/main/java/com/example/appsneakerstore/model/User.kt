package com.example.appsneakerstore.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val isGuest: Boolean = false
)

data class UserCredentials(
    val email: String,
    val password: String
)

data class RegistrationData(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
