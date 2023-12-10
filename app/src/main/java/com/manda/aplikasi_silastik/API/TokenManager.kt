package com.manda.aplikasi_silastik.API

import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("authToken", token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("authToken", null)
    }

    fun clearAuthToken() {
        val editor = sharedPreferences.edit()
        editor.remove("authToken")
        editor.apply()
    }
}
