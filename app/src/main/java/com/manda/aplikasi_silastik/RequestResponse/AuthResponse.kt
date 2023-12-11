package com.manda.aplikasi_silastik.RequestResponse

data class AuthResponse(
    val id: Long,
    val email: String,
    val accessToken: String,
    val roles: List<String>
)
