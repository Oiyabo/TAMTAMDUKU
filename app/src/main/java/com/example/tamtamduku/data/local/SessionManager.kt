package com.example.tamtamduku.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("voca_session", Context.MODE_PRIVATE)

    fun saveToken(token: String, userId: String) {
        prefs.edit()
            .putString("auth_token", token)
            .putString("user_id", userId)
            .putLong("login_timestamp", System.currentTimeMillis())
            .apply()
    }

    fun getToken(): String? {
        val token = prefs.getString("auth_token", null) ?: return null
        val loginTime = prefs.getLong("login_timestamp", 0L)
        if (System.currentTimeMillis() - loginTime > 10800000L) { // 3 hours in ms
            clearSession()
            return null
        }
        return token
    }

    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}
