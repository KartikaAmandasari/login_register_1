package com.manda.aplikasi_silastik.entity

import com.manda.aplikasi_silastik.RequestStatus

data class DataUserRequestDto(
    val id: Long,
    val status: RequestStatus,
    val data: DataUser
)
