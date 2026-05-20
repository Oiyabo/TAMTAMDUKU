package com.example.tamtamduku.data.remote

import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.google.gson.annotations.SerializedName

data class TrackingPekerjaanDto(
    @SerializedName("worker_name")
    val workerName: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("icon_type")
    val iconType: String
)

fun TrackingPekerjaanDto.toDomainModel(): TrackingPekerjaan {
    return TrackingPekerjaan(
        workerName = workerName,
        date = date,
        time = time,
        status = status,
        iconType = iconType
    )
}
