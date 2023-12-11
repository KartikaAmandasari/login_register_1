package com.manda.aplikasi_silastik

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.entity.DataUserRequestDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatDataActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_data)

        listView = findViewById(R.id.listViewRequestHistory)
        tokenManager = TokenManager(applicationContext)

        // Panggil fungsi untuk mengambil data
        fetchData()
    }

    private fun fetchData() {
        // Ambil ID pengguna dari SharedPreferences
        val userId = tokenManager.getUserId()

        // Panggil endpoint dengan ID pengguna
        userId?.let {
            fetchUserDataRequests(it)
        }
    }

    private fun fetchUserDataRequests(userId: Long) {
        // Buat instance dari ApiService
        val apiService = ApiServiceGenerator.createService(ApiService::class.java)

        // Panggil endpoint untuk mendapatkan data riwayat berdasarkan userId
        val call = apiService.getUserDataRequests(userId)

        // Lakukan permintaan secara asynchronous
        call.enqueue(object : Callback<List<DataUserRequestDto>> {
            override fun onResponse(
                call: Call<List<DataUserRequestDto>>,
                response: Response<List<DataUserRequestDto>>
            ) {
                if (response.isSuccessful) {
                    // Proses data jika permintaan berhasil
                    val dataUserRequestList = response.body()

                    // Tampilkan data dalam ListView
                    showDataInListView(dataUserRequestList)
                } else {
                    // Tampilkan pesan kesalahan dari server
                    val errorMessage = try {
                        response.errorBody()?.string() ?: "Unknown error"
                    } catch (e: Exception) {
                        "Error parsing error message"
                    }

                    Log.e("RiwayatDataActivity", "Error response: $response")
                    Log.e("RiwayatDataActivity", "Error message: $errorMessage")

                    Toast.makeText(
                        this@RiwayatDataActivity,
                        "Terjadi kesalahan: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<DataUserRequestDto>>, t: Throwable) {
                // Tampilkan pesan jika terjadi kesalahan
                Log.e("RiwayatDataActivity", "Terjadi kesalahan: ${t.message}")
                Toast.makeText(
                    this@RiwayatDataActivity,
                    "Terjadi kesalahan: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showDataInListView(dataList: List<DataUserRequestDto>?) {
        // Tampilkan data dalam ListView
        if (dataList != null) {
            val adapter = ArrayAdapter(
                this@RiwayatDataActivity,
                android.R.layout.simple_list_item_1,
                dataList.map { it.toString() }
            )
            listView.adapter = adapter
        }
    }
}
