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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Panggil fungsi register
            register(name, email, password)
        }
    }

    private fun register(name: String, email: String, password: String) {
        val apiService = ApiServiceGenerator.createService(ApiService::class.java)
        val request = UserDto(0, name, email, password)

        val call = apiService.register(request)
        call.enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful) {
                    // Registrasi berhasil, tampilkan Toast
                    showToast("Registrasi sukses")

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: kembali ke LoginActivity atau lakukan navigasi lainnya
                } else {
                    // Registrasi gagal, tampilkan Toast
                    showToast("Registrasi gagal")
                }
            }

//            override fun onFailure(call: Call<UserDto>, t: Throwable) {
//                // Tangani kegagalan koneksi atau permintaan
//                showToast("Terjadi kesalahan. Silakan coba lagi.")
//            }
                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    // Tangani kegagalan koneksi atau permintaan
                    showToast("Terjadi kesalahan: ${t.message}")
                    Log.e("RegisterActivity", "Error during registration", t)
                }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
