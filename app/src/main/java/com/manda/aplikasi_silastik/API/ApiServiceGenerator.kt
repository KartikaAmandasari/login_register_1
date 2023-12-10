package com.manda.aplikasi_silastik.API

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context

object ApiServiceGenerator {
    private const val BASE_URL = "http://192.168.100.116:8080/"

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    val retrofit = builder.build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}

//object ApiServiceGenerator {
//    private const val BASE_URL = "http://192.168.100.116:8080/"
//
//    private val httpClient = OkHttpClient.Builder()
//
//    private val tokenInterceptor = Interceptor { chain ->
//        val original = chain.request()
//
//        // Ambil konteks dari aplikasi atau kelas yang memiliki konteks
//        val context = YourApplicationContextProvider.getContext()
//
//        // Pastikan context tidak null sebelum mengakses TokenManager
//        val token = TokenManager(context).getAuthToken()
//
//        val requestBuilder = original.newBuilder()
//            .header("Authorization", "Bearer $token")
//            .method(original.method(), original.body())
//
//        val request = requestBuilder.build()
//        chain.proceed(request)
//    }
//
//    private val builder = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient.addInterceptor(tokenInterceptor).build())
//
//    private val retrofit = builder.build()
//
//    fun <S> createService(serviceClass: Class<S>): S {
//        return retrofit.create(serviceClass)
//    }
//}
