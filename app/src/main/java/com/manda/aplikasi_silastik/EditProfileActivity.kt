package com.manda.aplikasi_silastik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.entity.UpdatedUserDto
import com.manda.aplikasi_silastik.entity.UserDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var apiService: ApiService
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editTextName = findViewById(R.id.updatedNameEditText)
        editTextEmail = findViewById(R.id.updatedEmailEditText)
        btnSave = findViewById(R.id.saveProfileButton)

        apiService = ApiServiceGenerator.getUserService()
        tokenManager = TokenManager(applicationContext)

        loadUserProfile()

        btnSave.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserProfile() {
        val userId = tokenManager.getUserId()
        userId?.let {
            val authToken = "Bearer ${tokenManager.getAuthToken()}"
            apiService.getUserProfile(authToken).enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                    if (response.isSuccessful) {
                        val userDto = response.body()
                        editTextName.setText(userDto?.name)
                        editTextEmail.setText(userDto?.email)
                    } else {

                    }
                }

                override fun onFailure(call: Call<UserDto>, t: Throwable) {

                }
            })
        }
    }

    private fun saveUserProfile() {
        val userId = tokenManager.getUserId()
        userId?.let {
            val authToken = "Bearer ${tokenManager.getAuthToken()}"
            val updatedName = editTextName.text.toString()
            val updatedEmail = editTextEmail.text.toString()

            val updatedUserDto = UpdatedUserDto(it, updatedName, updatedEmail)

            apiService.updateUserProfile(authToken, it, updatedUserDto)
                .enqueue(object : Callback<UpdatedUserDto> {
                    override fun onResponse(
                        call: Call<UpdatedUserDto>,
                        response: Response<UpdatedUserDto>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Update profil berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(
                                this@EditProfileActivity,
                                "Gagal menyimpan perubahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdatedUserDto>, t: Throwable) {

                    }
                })
        }
    }
}
