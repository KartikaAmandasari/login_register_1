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

        dataAccessButton.setOnClickListener {
            // Tambahkan logika untuk tombol Layanan Akses Data
        }

        statisticsConsultationButton.setOnClickListener {
            // Tambahkan logika untuk tombol Konsultasi Statistik
        }

        profileIcon.setOnClickListener {
            // Ketika ikon gambar orang di klik, arahkan ke ProfileActivity
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
