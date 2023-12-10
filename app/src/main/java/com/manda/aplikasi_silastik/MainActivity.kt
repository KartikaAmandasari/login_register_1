package com.manda.aplikasi_silastik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataAccessButton: Button = findViewById(R.id.dataAccessButton)
        val statisticsConsultationButton: Button = findViewById(R.id.statisticsConsultationButton)
        val profileIcon: ImageView = findViewById(R.id.profileIcon)
        val historyDataButton: Button = findViewById(R.id.historyDataButton)

        dataAccessButton.setOnClickListener {
            val intent = Intent(this@MainActivity, DataActivity::class.java)
            startActivity(intent)
        }

        statisticsConsultationButton.setOnClickListener {
            // Tambahkan logika untuk tombol Konsultasi Statistik
            val intent = Intent(this@MainActivity, KonsultasiActivity::class.java)
            startActivity(intent)
        }

        //sementara
        historyDataButton.setOnClickListener {
            val intent = Intent(this@MainActivity, RiwayatDataActivity::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            // Ketika ikon gambar orang di klik, arahkan ke ProfileActivity
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
