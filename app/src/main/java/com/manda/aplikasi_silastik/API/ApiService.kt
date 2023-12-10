package com.manda.aplikasi_silastik.API

import com.manda.aplikasi_silastik.AuthRequest
import com.manda.aplikasi_silastik.AuthResponse
import com.manda.aplikasi_silastik.UserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    @POST("/register")
    fun register(@Body request: UserDto): Call<UserDto>

}
