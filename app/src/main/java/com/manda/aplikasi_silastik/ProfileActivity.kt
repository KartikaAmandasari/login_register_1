package com.manda.aplikasi_silastik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.entity.UserDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var changeProfileButton: Button
    private lateinit var changePasswordButton: Button
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameTextView = findViewById(R.id.nameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        changeProfileButton = findViewById(R.id.changeProfileButton)
        changePasswordButton = findViewById(R.id.changePasswordButton)

        // Inisialisasi TokenManager dengan melewatkan context
        tokenManager = TokenManager(this)

        // Panggil fungsi untuk mendapatkan profil pengguna
        getUserProfile()

        changeProfileButton.setOnClickListener {
            Log.d("ProfileActivity", "Button clicked, starting EditProfileActivity")
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        changePasswordButton.setOnClickListener {
            Log.d("ProfileActivity", "Button clicked, starting EditProfileActivity")
            val intent = Intent(this@ProfileActivity, EditPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserProfile() {
        // Ambil token dari TokenManager
        val authToken = tokenManager.getAuthToken()

        if (authToken != null) {
            val apiService = ApiServiceGenerator.createService(ApiService::class.java)
            val call = apiService.getUserProfile("Bearer $authToken")

            call.enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                    if (response.isSuccessful) {
                        val userDto = response.body()
                        showUserProfile(userDto)
                    } else {
                        if (response.code() == 401) {
                            showToast("Token kadaluwarsa. Silakan login kembali.")
                            logOut()
                        } else {
                            showToast("Gagal mendapatkan profil pengguna")
                        }
                    }
                }

                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    showToast("Terjadi kesalahan: ${t.message}")
                    Log.e("ProfileActivity", "Error while getting user profile", t)
                }
            })
        } else {
            showToast("Token tidak tersedia. Silakan login kembali.")
            logOut()
        }
    }

    private fun showUserProfile(userDto: UserDto?) {
        if (userDto != null) {
            // Tampilkan nama dan email pengguna di TextView
            nameTextView.text = "Nama: ${userDto.name}"
            emailTextView.text = "Email: ${userDto.email}"
        } else {
            showToast("Gagal mendapatkan profil pengguna")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logOut() {
        // Tambahkan logika logout di sini (hapus token, redirect ke halaman login, dll.)
    }
}
