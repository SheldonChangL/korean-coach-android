package com.koreancoach.app.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.koreancoach.app.R

object NotificationHelper {
    const val CHANNEL_ID = "daily_reminder"
    const val CHANNEL_NAME = "Daily Study Reminder"
    const val NOTIFICATION_ID = 1001

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Reminds you to study Korean every day"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showReminder(context: Context) {
        val messages = listOf(
            "Time to study Korean! 한국어 공부할 시간이에요! 🇰🇷",
            "안녕하세요! Your daily Korean lesson is waiting 📚",
            "화이팅! Keep your streak alive — study now! 🔥",
            "Did you know? 5 minutes a day builds lasting fluency! ⏰",
            "Your Korean words are waiting — don't let them down! 💪"
        )
        val message = messages.random()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Korean Coach")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        runCatching {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        }
    }
}
