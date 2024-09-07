package com.example.jetpackshop.Websocket.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.jetpackshop.R

@SuppressLint("MissingPermission")
fun showNotification(context: Context, title: String, message: String) {
    val channelId = "chat_message_channel"
    val notificationId = 1

    // ساخت کانال اعلان (برای نسخه‌های بالاتر از اندروید 8)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Chat Message Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for chat message notifications"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // ساخت اعلان
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_notifications_24)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification)
    }
}
