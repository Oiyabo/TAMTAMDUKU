package com.example.tamtamduku

import android.app.Application
import android.content.Context

class TamtamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
