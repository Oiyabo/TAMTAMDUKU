package com.example.tamtamduku.payment

import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object PaymentEventBus {
    private val _paymentResult = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 1)
    val paymentResult = _paymentResult.asSharedFlow()
    
    fun emitSuccess() {
        _paymentResult.tryEmit(true)
    }

    fun reset() {
        _paymentResult.tryEmit(false)
    }
}

class PaymentRepository {
    private val functions: FirebaseFunctions = Firebase.functions

    suspend fun getSnapToken(orderId: String, amount: Long, name: String, email: String): String {
        val data = hashMapOf(
            "orderId" to orderId,
            "amount" to amount,
            "name" to name,
            "email" to email
        )

        val result = functions
            .getHttpsCallable("createPayment")
            .call(data)
            .await()

        val resultData = result.data as? Map<*, *>
        return resultData?.get("token") as? String ?: throw Exception("Token not found in response")
    }
}
