package com.manda.aplikasi_silastik.API

import android.app.Application
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context



object ApiServiceGenerator {
    private const val BASE_URL = "http://192.168.100.116:8080/"
    private lateinit var applicationContext: Context

    fun initialize(applicationContext: Application) {
        this.applicationContext = applicationContext
    }

    private val interceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer " + TokenManager(applicationContext).getAuthToken())
            .method(original.method(), original.body())
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)

    val retrofit = builder.build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
    fun getUserService(): ApiService {
        return createService(ApiService::class.java)
    }

}

