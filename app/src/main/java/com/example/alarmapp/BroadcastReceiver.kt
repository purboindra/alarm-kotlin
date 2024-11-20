package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, @SuppressLint("UnsafeIntentLaunch") intent: Intent) {
        Log.d("AlarmReceiver", "Alarm triggered!")
        val startDate = intent.getLongExtra("start_date", -1)
        val endDate = intent.getLongExtra("end_date", -1)

        // Check if the current time is within the range
        val currentTime = System.currentTimeMillis()

        Log.d(
            "AlarmReceiver",
            "Alarm triggered! Start: $startDate, End: $endDate, CurrentTime: $currentTime"
        )


//        if (currentTime > endDate) {
//            Log.d("AlarmReceiver", "End date exceeded, canceling alarm.")
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            val pendingIntent = PendingIntent.getBroadcast(
//                context, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            alarmManager.cancel(pendingIntent)
//            return
//        }

        showNotification(context, "Alarm Triggered", "This is your scheduled alarm.")
    }

}
