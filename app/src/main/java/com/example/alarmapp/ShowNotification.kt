package com.example.alarmapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.app.PendingIntent
import android.content.Intent


fun showNotification(context: Context, title: String, message: String) {
    val channelId = "alarm_channel"
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Permission", "POST_NOTIFICATIONS permission not granted")
            return
        }
    }
    
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Alarm Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
    
    val intent = Intent(context, MainActivity::class.java).apply {
        putExtra("url", "https://play.google.com/store/search?q=grab&c=apps&hl=id")
    }
    
    val pendingIntent = PendingIntent.getActivity(
        context, 0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    
    val action =
        NotificationCompat.Action(android.R.drawable.arrow_down_float, "Open App", pendingIntent)
    
    val notification = NotificationCompat.Builder(context, channelId).setContentTitle(title)
        .setContentText(message).setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setPriority(NotificationCompat.PRIORITY_HIGH).addAction(action).setAutoCancel(true).build()
    
    NotificationManagerCompat.from(context).notify(1, notification)
}