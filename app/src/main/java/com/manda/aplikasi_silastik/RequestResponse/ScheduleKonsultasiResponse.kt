package com.manda.aplikasi_silastik.RequestResponse

import java.time.LocalDate

data class ScheduleKonsultasiResponse(
    val id: Long,
    val userName: String,
    val tanggal: LocalDate,
)
