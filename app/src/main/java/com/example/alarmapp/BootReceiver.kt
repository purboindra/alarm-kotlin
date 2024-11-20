package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.lang.reflect.Array.set
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("onReceive BootReceiver", intent?.action.toString())

        if (intent != null && Intent.ACTION_BOOT_COMPLETED == intent.action) {
            context?.let {
                // Re-schedule alarms
                scheduleDailyAlarm(it)
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleDailyAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9) // Example: 9 AM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        Log.d("BootReceiver", "ScheduleDailyAlarm: ${startDate.time}")

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            startDate.timeInMillis,
            pendingIntent
        )
    }
}
