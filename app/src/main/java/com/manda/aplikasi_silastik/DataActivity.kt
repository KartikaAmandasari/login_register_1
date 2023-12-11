package com.manda.aplikasi_silastik

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.manda.aplikasi_silastik.API.ApiService
import com.manda.aplikasi_silastik.API.ApiServiceGenerator
import com.manda.aplikasi_silastik.API.TokenManager
import com.manda.aplikasi_silastik.RequestResponse.DataUserRequest
import com.manda.aplikasi_silastik.entity.DataUser
import com.manda.aplikasi_silastik.entity.UserDto
import retrofit2.Response as RetrofitResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var tokenManager: TokenManager
    private lateinit var listView: ListView
    private lateinit var dataAdapter: ArrayAdapter<DataUser>

    private var selectedData: DataUser? = null
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        apiService = ApiServiceGenerator.createService(ApiService::class.java)
        tokenManager = TokenManager(applicationContext)

        // Mendapatkan email pengguna dari profil
        getUserProfile()

        // Inisialisasi ListView
        listView = findViewById(R.id.listViewData)

        // Mendapatkan data dari server
        getAllDataFromServer()

        // Menerapkan klik item di ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedData = dataAdapter.getItem(position)
            showDataRequestDialog()
        }
    }

    private fun getUserProfile() {
        val authToken = tokenManager.getAuthToken()
        authToken?.let {
            apiService.getUserProfile("Bearer $it").enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: RetrofitResponse<UserDto>) {
                    if (response.isSuccessful) {
                        userEmail = response.body()?.email
                    } else {

                    }
                }

                override fun onFailure(call: Call<UserDto>, t: Throwable) {

                }
            })
        }
    }

    private fun getAllDataFromServer() {
        apiService.getAllData("Bearer ${tokenManager.getAuthToken()}").enqueue(object : Callback<List<DataUser>> {
            override fun onResponse(call: Call<List<DataUser>>, response: RetrofitResponse<List<DataUser>>) {
                if (response.isSuccessful) {
                    val dataList = response.body()
                    dataList?.let {
                        displayDataList(it)
                    }
                } else {
                    // Handle response error
                    // ...
                }
            }

            override fun onFailure(call: Call<List<DataUser>>, t: Throwable) {

            }
        })
    }

    private fun displayDataList(dataList: List<DataUser>) {
        // Menyimpan data ke dalam adapter dan menampilkan di ListView
        dataAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = dataAdapter
    }

    private fun showDataRequestDialog() {
        selectedData?.let {
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Ajukan permintaan data ini?")
                .setMessage("Anda yakin ingin mengajukan permintaan data ini?")
                .setPositiveButton("Ya") { _, _ ->
                    userEmail?.let { email ->
                        requestDataToServer(it.id, email)
                    }
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()  // Menutup popup
                }
                .show()
        }
    }

    private fun requestDataToServer(dataId: Long, Email: String) {
        val requestData = DataUserRequest(dataId, Email)

        // Melakukan pemanggilan ke endpoint /data/request
        apiService.requestData("Bearer ${tokenManager.getAuthToken()}", requestData)
            .enqueue(object : Callback<Response<String>> {
                override fun onResponse(call: Call<Response<String>>, response: RetrofitResponse<Response<String>>) {
                    if (response.isSuccessful) {

                    } else {

                    }
                }

                override fun onFailure(call: Call<Response<String>>, t: Throwable) {

                }
            })
    }
}
