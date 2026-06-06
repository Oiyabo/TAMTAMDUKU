package com.example.tamtamduku.data.network

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val accessToken: String,
    val refreshToken: String
)

interface DummyJsonApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
