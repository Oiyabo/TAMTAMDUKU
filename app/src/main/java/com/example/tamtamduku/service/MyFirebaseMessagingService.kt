package com.example.tamtamduku.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.tamtamduku.MainActivity
import com.example.tamtamduku.R
import com.example.tamtamduku.core.util.SessionManager
import com.example.tamtamduku.data.repository.WorkerRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val repository = WorkerRepository()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        
        // Save token to Firestore if user is logged in
        val sessionManager = SessionManager(applicationContext)
        val userId = sessionManager.getUserId()
        if (userId != null) {
            scope.launch {
                try {
                    repository.updateUserFcmToken(userId, token)
                    Log.d("FCM", "Token updated in Firestore")
                } catch (e: Exception) {
                    Log.e("FCM", "Failed to update token: ${e.message}")
                }
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "From: ${remoteMessage.from}")

        // Read from data payload first
        val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title ?: "TamtamDuku"
        val body = remoteMessage.data["body"] ?: remoteMessage.notification?.body ?: "Anda memiliki pesan baru"
        val targetRoute = remoteMessage.data["targetRoute"]

        Log.d("FCM", "Message Title: $title, Body: $body")
        
        handleNotification(title, body, targetRoute)
    }

    private fun handleNotification(title: String, body: String, targetRoute: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            if (targetRoute != null) {
                putExtra("targetRoute", targetRoute)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "tamtamduku_notifications"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Use your app's notification icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(
            channelId,
            "TamtamDuku Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "General notifications from TamtamDuku"
        }
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
