package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

fun schedulerAlarm(context: Context, alarmTimer: java.util.Calendar) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        if (alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimer.timeInMillis, pendingIntent)
        }
    }
}


//SCHEDULE ALARM BY DAILY
fun scheduleDailyAlarm(
    context: Context,
    startDate: Calendar,
    endDate: Calendar,
    hour: Int,
    minute: Int
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmId = 1
    
    startDate.set(Calendar.HOUR_OF_DAY, hour)
    Log.d("startDate", "After setting HOUR_OF_DAY: ${startDate.time}")
    startDate.set(Calendar.MINUTE, minute)
    Log.d("startDate", "After setting MINUTE: ${startDate.time}")
    startDate.set(Calendar.SECOND, 0)
    Log.d("startDate", "After setting SECOND: ${startDate.time}")
    
    Log.d("startDate", "Input Hour: $hour, Input Minute: $minute")
    Log.d("startDate", startDate.time.toString())
    Log.d("endDate", endDate.time.toString())
    
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("alarm_id", alarmId)
        putExtra("start_date", startDate.timeInMillis)
        putExtra("end_date", endDate.timeInMillis)
    }
    
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        startDate.timeInMillis,
        System.currentTimeMillis() + 60000L,
        pendingIntent
    )
}