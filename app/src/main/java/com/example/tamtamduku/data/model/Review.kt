package com.example.tamtamduku.data.model

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("reviews")
    val reviews: List<Review>
)

data class Review(
    @SerializedName("worker_name")
    val workerName: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("review_text")
    val reviewText: String,
    @SerializedName("date")
    val date: String
)
