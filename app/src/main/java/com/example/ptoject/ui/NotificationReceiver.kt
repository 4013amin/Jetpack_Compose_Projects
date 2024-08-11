import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notification_id", 0)
        val title = intent.getStringExtra("notification_title")
        val message = intent.getStringExtra("notification_message")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "your_channel_id"

        val notificationChannel = NotificationChannel(notificationChannelId, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Notification Channel Description"
        }
        notificationManager.createNotificationChannel(notificationChannel)

        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
