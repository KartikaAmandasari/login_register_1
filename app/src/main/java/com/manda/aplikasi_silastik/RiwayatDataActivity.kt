package com.manda.aplikasi_silastik

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.RequestResponse.DataUserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatDataActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var tokenManager: TokenManager
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_data)

        apiService = ApiServiceGenerator.createService(ApiService::class.java)
        tokenManager = TokenManager(applicationContext)

        listView = findViewById(R.id.listViewRequestHistory)

        // Ambil token dari TokenManager
        val authToken = tokenManager.getAuthToken()

        // Panggil endpoint untuk mendapatkan riwayat pengajuan data
        val call: Call<List<DataUser>> = apiService.getSomeData("Bearer $authToken") // Sesuaikan dengan parameter yang sesuai
        call.enqueue(object : Callback<List<DataUser>> {
            override fun onResponse(call: Call<List<DataUser>>, response: Response<List<DataUser>>) {
                if (response.isSuccessful) {
                    val userDataList = response.body()

                    // Ambil data atribut dari riwayat pengajuan
                    val requestDataDetails = getRequestDataDetails(userDataList)

                    // Inisialisasi adapter dan set ke ListView
                    adapter = ArrayAdapter(this@RiwayatDataActivity, android.R.layout.simple_list_item_1, requestDataDetails)
                    listView.adapter = adapter
                } else {
                    // Tangani respons tidak sukses di sini
                }
            }

            override fun onFailure(call: Call<List<DataUser>>, t: Throwable) {
                // Tangani kegagalan panggilan di sini
            }
        })
    }

    // Metode untuk mendapatkan detail atribut dari objek DataUser
    private fun getRequestDataDetails(userDataList: List<DataUser>?): List<String> {
        // Implementasikan logika sesuai dengan struktur DataUser Anda
        // Misalnya, dapatkan seluruh atribut dari setiap objek dan masukkan ke dalam List<String>
        // Contoh sederhana: ambil semua atribut dari objek DataUser
        val requestDataDetails = mutableListOf<String>()
        userDataList?.let {
            for (dataUser in it) {
                val detail = "ID: ${dataUser.id}, Kategori: ${dataUser.kategori}, Judul: ${dataUser.judul}, Provinsi: ${dataUser.provinsi}, Tahun: ${dataUser.tahun}"
                requestDataDetails.add(detail)
            }
        }
        return requestDataDetails
    }
}
