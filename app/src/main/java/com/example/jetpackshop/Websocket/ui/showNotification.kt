import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.jetpackshop.R

fun showNotification(context: Context, title: String, message: String) {
    val channelId = "chat_channel"
    val notificationId = 1

    // ایجاد کانال اعلان برای نسخه‌های Android O و بالاتر
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Chat Messages"
        val descriptionText = "Notifications for new chat messages"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        // ثبت کانال در سیستم
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // ایجاد اعلان
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_notifications_24)  // آیکون اعلان
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // نمایش اعلان
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}
