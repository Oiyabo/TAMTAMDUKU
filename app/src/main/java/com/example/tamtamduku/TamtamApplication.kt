package com.example.tamtamduku

import android.app.Application
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class TamtamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMidtransSdk()
    }

    private fun initMidtransSdk() {
        val clientKey = BuildConfig.MIDTRANS_CLIENT_KEY

        SdkUIFlowBuilder.init()
            .setClientKey(if (clientKey.isEmpty()) "DUMMY_KEY" else clientKey)
            .setContext(this)
            .setTransactionFinishedCallback { result ->
                // Handle global transaction result if needed, or handle it per activity
            }
            .setMerchantBaseUrl("https://tamtamduku-9647d.web.app/") // Placeholder base URL
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()
    }
}
