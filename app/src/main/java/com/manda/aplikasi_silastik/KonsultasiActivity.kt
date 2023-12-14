package com.manda.aplikasi_silastik

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.entity.Konsultasi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class KonsultasiActivity : AppCompatActivity() {

    private lateinit var tanggalTextView: TextView
    private lateinit var pilihTanggalButton: Button
    private lateinit var namaEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var jadwalkanButton: Button
    private lateinit var tokenManager: TokenManager

    // Variabel untuk menampilkan tanggal di antarmuka pengguna
    private var selectedDateForDisplay: String = ""

    // Variabel untuk menampung tanggal yang akan dikirim ke server
    private var selectedDateForServer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konsultasi)

        tanggalTextView = findViewById(R.id.tanggalTextView)
        pilihTanggalButton = findViewById(R.id.pilihTanggalButton)
        namaEditText = findViewById(R.id.namaEditText)
        emailEditText = findViewById(R.id.emailEditText)
        jadwalkanButton = findViewById(R.id.jadwalkanButton)

        tokenManager = TokenManager(this)

        pilihTanggalButton.setOnClickListener {
            showDatePicker()
        }

        jadwalkanButton.setOnClickListener {
            scheduleKonsultasi()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                // Atur variabel untuk menampilkan tanggal di antarmuka pengguna
                selectedDateForDisplay = dateFormat.format(selectedDate.time)
                tanggalTextView.text = selectedDateForDisplay

                // Atur variabel untuk tanggal yang akan dikirim ke server
                selectedDateForServer = selectedDateForDisplay
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun scheduleKonsultasi() {
        val apiService = ApiServiceGenerator.createService(ApiService::class.java)
        val authToken = tokenManager.getAuthToken()

        val tanggal = selectedDateForServer
        val nama = namaEditText.text.toString()
        val email = emailEditText.text.toString()

        if (authToken != null) {
            val konsultasi = Konsultasi(nama, email, tanggal)

            val call = apiService.scheduleKonsultasi("Bearer $authToken", konsultasi)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val successMessage = response.body() // Mengambil pesan sukses dari respons body
                        Log.e("KonsultasiActivity", "Penjadwalan berhasil: $successMessage")
                        showToast(successMessage ?: "Penjadwalan berhasil")
                        finish()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Gagal menjadwalkan konsultasi"
                        Log.d("KonsultasiActivity", "Gagal menjadwalkan konsultasi: $errorMessage")
                        showToast(errorMessage)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    showToast("Terjadi kesalahan: ${t.message}")
                }
            })
        } else {
            showToast("Token tidak tersedia. Silakan login kembali.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
