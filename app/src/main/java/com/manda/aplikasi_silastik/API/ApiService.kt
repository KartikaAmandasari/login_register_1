package com.manda.aplikasi_silastik.API

import com.manda.aplikasi_silastik.RequestResponse.AuthRequest
import com.manda.aplikasi_silastik.RequestResponse.AuthResponse
import com.manda.aplikasi_silastik.entity.DataUser
import com.manda.aplikasi_silastik.entity.Konsultasi
import com.manda.aplikasi_silastik.RequestResponse.DataUserRequest
import com.manda.aplikasi_silastik.entity.DataUserRequestDto
import com.manda.aplikasi_silastik.entity.UserDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    @POST("/register")
    fun register(@Body request: UserDto): Call<UserDto>
    @GET("/profile")
    fun getUserProfile(@Header("Authorization") authToken: String): Call<UserDto>

    @POST("/konsultasi/schedule")
    fun scheduleKonsultasi(@Header("Authorization") authToken: String, @Body konsultasi: Konsultasi): Call<String>

    @GET("/data/katalog")
    fun getAllData(@Header("Authorization") authToken: String): Call<List<DataUser>>

    @POST("/data/request")
    fun requestData (@Header("Authorization") authToken: String, @Body requestData: DataUserRequest): Call<Response<String>>

    @GET("/data/requests/user/{id}")
    fun getUserDataRequests(@Path("id") id: Long): Call<List<DataUserRequestDto>>

}
