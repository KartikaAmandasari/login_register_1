package com.manda.aplikasi_silastik

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class KonsultasiActivity : AppCompatActivity() {

    private lateinit var editTextNama: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var buttonSubmit: Button

    private val apiService = ApiServiceGenerator.createService(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konsultasi)

        editTextNama = findViewById(R.id.editTextNama)
        editTextEmail = findViewById(R.id.editTextEmail)
        datePicker = findViewById(R.id.datePicker)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val nama = editTextNama.text.toString()
            val email = editTextEmail.text.toString()
            val tanggal = getSelectedDate()

            val konsultasi = Konsultasi(name = nama, email = email, tanggal = tanggal)

            val call = apiService.scheduleKonsultasi("Bearer " + TokenManager(this).getAuthToken(), konsultasi)

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val message = response.body()
                        Toast.makeText(
                            this@KonsultasiActivity,
                            "Konsultasi berhasil dijadwalkan. Pesan: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Kembali ke MainActivity setelah sukses
                    } else {
                        Toast.makeText(
                            this@KonsultasiActivity,
                            "Gagal mengirim konsultasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        this@KonsultasiActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun getSelectedDate(): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1 // Month is zero-based
        val year = datePicker.year

        // Format tanggal sesuai dengan yang dibutuhkan oleh backend
        return "$year-$month-$day"
    }
}
