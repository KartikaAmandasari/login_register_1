package com.manda.aplikasi_silastik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.entity.PasswordChangeRequest
import com.manda.aplikasi_silastik.entity.UpdatedUserDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var editTextOldPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var btnSavePassword: Button
    private lateinit var apiService: ApiService
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        editTextOldPassword = findViewById(R.id.oldPasswordEditText)
        editTextNewPassword = findViewById(R.id.newPasswordEditText)
        btnSavePassword = findViewById(R.id.savePasswordButton)

        apiService = ApiServiceGenerator.getUserService()
        tokenManager = TokenManager(applicationContext)

        btnSavePassword.setOnClickListener {
            saveUserPassword()
        }
    }

    private fun saveUserPassword() {
        val userId = tokenManager.getUserId()

        userId?.let {
            val authToken = "Bearer ${tokenManager.getAuthToken()}"
            val oldPassword = editTextOldPassword.text.toString()
            val newPassword = editTextNewPassword.text.toString()

            val passwordChangeRequest = PasswordChangeRequest(oldPassword, newPassword)

            apiService.changePassword(authToken, it, passwordChangeRequest)
                .enqueue(object : Callback<UpdatedUserDto> {
                    override fun onResponse(
                        call: Call<UpdatedUserDto>,
                        response: Response<UpdatedUserDto>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@EditPasswordActivity,
                                "Perubahan password berhasil disimpan",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@EditPasswordActivity, ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(
                                this@EditPasswordActivity,
                                "Gagal menyimpan perubahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                    override fun onFailure(call: Call<UpdatedUserDto>, t: Throwable) {

                        Toast.makeText(
                            this@EditPasswordActivity,
                            "Terjadi kesalahan: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}