//package com.manda.aplikasi_silastik.API
//
//import okhttp3.Interceptor
//import okhttp3.Response
//
//class AuthInterceptor(private val authToken: String) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val original = chain.request()
//        val requestBuilder = original.newBuilder()
//            .header("Authorization", "Bearer $authToken")
//        val request = requestBuilder.build()
//        return chain.proceed(request)
//    }
//}
