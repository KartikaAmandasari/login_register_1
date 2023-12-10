package com.manda.aplikasi_silastik

data class AuthResponse(
    val email: String,
    val accessToken: String,
    val roles: List<String>
)
