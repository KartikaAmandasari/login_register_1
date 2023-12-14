package com.manda.aplikasi_silastik.entity

import com.google.gson.annotations.SerializedName

data class PasswordChangeRequest(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String
)