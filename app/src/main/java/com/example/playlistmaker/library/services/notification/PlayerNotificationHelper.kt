package com.example.playlistmaker.library.services.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.core.domain.model.Song

class PlayerNotificationHelper(val context: Context) {
    fun createChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            ContextCompat.getString(
                context,
                R.string.app_name
            ),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notifManager = ContextCompat.getSystemService(context, NotificationManager::class.java)

        notifManager?.createNotificationChannel(channel)
    }

    fun buildNotification(song: Song): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(
                ContextCompat.getString(
                    context,
                    R.string.app_name
                )
            )
            .setContentText("${song.artistName} - ${song.trackName}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private companion object {
        private const val NOTIFICATION_CHANNEL_ID = "notificationId"
    }
}