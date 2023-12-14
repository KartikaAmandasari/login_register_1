package com.manda.aplikasi_silastik

import android.content.Intent
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

        fetchData()
    }

    private fun fetchData() {
        val userId = tokenManager.getUserId()
        userId?.let {
            fetchUserDataRequests(it)
        }
    }

    private fun fetchUserDataRequests(userId: Long) {
        val apiService = ApiServiceGenerator.createService(ApiService::class.java)
        val call = apiService.getUserDataRequests(userId)

        call.enqueue(object : Callback<List<DataUserRequestDto>> {
            override fun onResponse(
                call: Call<List<DataUserRequestDto>>,
                response: Response<List<DataUserRequestDto>>
            ) {
                if (response.isSuccessful) {
                    val dataUserRequestList = response.body()
                    showDataInListView(dataUserRequestList)
                    Toast.makeText(
                        this@RiwayatDataActivity,
                        "Request data berhasil",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RiwayatDataActivity, RiwayatDataActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
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

                    Toast.makeText(
                        this@RiwayatDataActivity,
                        "Request data gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<DataUserRequestDto>>, t: Throwable) {
                Log.e("RiwayatDataActivity", "Terjadi kesalahan: ${t.message}")
                Toast.makeText(
                    this@RiwayatDataActivity,
                    "Terjadi kesalahan: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()

                Toast.makeText(
                    this@RiwayatDataActivity,
                    "Request data gagal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showDataInListView(dataList: List<DataUserRequestDto>?) {
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
