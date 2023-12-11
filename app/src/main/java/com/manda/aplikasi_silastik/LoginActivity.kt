package com.manda.aplikasi_silastik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.RequestResponse.AuthRequest
import com.manda.aplikasi_silastik.RequestResponse.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Inisialisasi TokenManager
        tokenManager = TokenManager(this)

        // Inisialisasi ApiServiceGenerator dengan applicationContext
        ApiServiceGenerator.initialize(application)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Panggil fungsi login
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val apiService = ApiServiceGenerator.createService(ApiService::class.java)
        val request = AuthRequest(email, password)

        val call = apiService.login(request)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()

                    // Cek apakah token dan ID pengguna tidak kosong
                    val token = authResponse?.accessToken
                    val userId = authResponse?.id

                    if (!token.isNullOrBlank() && userId != null) {
                        // Simpan token dan ID pengguna ke TokenManager
                        tokenManager.saveAuthToken(token)
                        tokenManager.saveUserId(userId)

                        // Tampilkan toast "Login Sukses"
                        showToast("Login Sukses")

                        // Arahkan ke MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: tutup activity ini agar tidak bisa kembali ke LoginActivity
                    } else {
                        // Tangani jika token atau ID pengguna kosong
                        showToast("Login Gagal: Token atau ID pengguna kosong")
                    }
                } else {
                    // Tangani respon yang tidak berhasil
                    showToast("Login Gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                // Tangani kegagalan koneksi atau permintaan
                showToast("Terjadi kesalahan: ${t.message}")
                Log.e("LoginActivity", "Error during login", t)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
