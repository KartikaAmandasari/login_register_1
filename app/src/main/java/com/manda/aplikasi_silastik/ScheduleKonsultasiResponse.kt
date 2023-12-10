package com.manda.aplikasi_silastik

import java.time.LocalDate

data class ScheduleKonsultasiResponse(
    val id: Long,
    val userName: String,
    val tanggal: LocalDate,
)
