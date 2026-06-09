package com.example.tamtamduku

import android.app.Application
import android.content.Context
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class TamtamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        // Initialize Midtrans SDK
        SdkUIFlowBuilder.init()
            .setClientKey(BuildConfig.MIDTRANS_CLIENT_KEY)
            .setContext(this)
            .setTransactionFinishedCallback { result ->
                if (result.status == com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS ||
                    result.status == com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING) {
                    com.example.tamtamduku.payment.PaymentEventBus.emitSuccess()
                }
            }
            .setMerchantBaseUrl("https://api.sandbox.midtrans.com/v2/") // Default sandbox API url
            .enableLog(true)
            .buildSDK()
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
