package com.example.alarmapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.lang.reflect.Array.set
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {
    @SuppressLint("InlinedApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        
        Log.d("AlarmReceiver", "Alarm triggered!")
        
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        
        
        if (intent != null && Intent.ACTION_BOOT_COMPLETED == intent.action) {
            if (isPermissionGranted) {
                showNotification(context!!)
            } else {
                Log.d("AlarmReceiver", "Permission not granted")
            }
        } else {
            Log.d("AlarmReceiver", "Else Block Intent Action")
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context) {
        Log.d("Show notification", "Firing!!!")
        val channelId = "alarm_channel"
        val channelName = "Alarm Notification"
        
        Log.d("Show notification", channelId)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Alarm Triggered")
            .setContentText("Your alarm has been triggered.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        NotificationManagerCompat.from(context).notify(1, notification)
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1, notification)
        } else {
            Log.e("Notification", "POST_NOTIFICATIONS permission not granted")
        }
    }
}
