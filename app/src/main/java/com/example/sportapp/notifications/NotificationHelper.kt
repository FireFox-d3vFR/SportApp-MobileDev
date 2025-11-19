package com.example.sportapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sportapp.R

object NotificationHelper {
    private const val CHANNEL_ID = "sportapp_general"

    private fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "General", NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    fun show(ctx: Context, title: String, message: String, id: Int = 1) {
        ensureChannel(ctx)
        val n = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(ctx).notify(id, n)
    }
}