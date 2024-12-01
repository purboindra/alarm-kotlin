package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alarmapp.utils.ScheduleType
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, @SuppressLint("UnsafeIntentLaunch") intent: Intent) {
        Log.d("AlarmReceiver", "Alarm triggered!")
        val startDate = intent.getLongExtra("start_date", -1)
        val endDate = intent.getLongExtra("end_date", -1)
        val currentTime = System.currentTimeMillis()
        
        Log.d(
            "AlarmReceiver",
            "Alarm triggered! Start: $startDate, End: $endDate, CurrentTime: $currentTime"
        )
        
        if (currentTime > endDate) {
            showNotification(context, "Alarm Triggered", "This is your last scheduled alarm.")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            return
        }
        showNotification(context, "Alarm Triggered", "This is your scheduled alarm.")
    }
}

//SCHEDULE ALARM BY DAILY
fun scheduleDailyAlarm(
    context: Context,
    startDate: Calendar,
    endDate: Calendar,
    hour: Int,
    minute: Int,
    type: ScheduleType
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmId = 1
    
    startDate.set(Calendar.HOUR_OF_DAY, hour)
    startDate.set(Calendar.MINUTE, minute)
    startDate.set(Calendar.SECOND, 0)

    Log.d("startDate", "Input Hour: $hour, Input Minute: $minute")
    Log.d("startDate", startDate.time.toString())
    Log.d("endDate", endDate.time.toString())
    
    Log.d("startDate", "timeInMillis: ${startDate.timeInMillis}")
    Log.d("endDate", "timeInMillis: ${endDate.timeInMillis}")
    
    Log.d("type", "Schedule Type: ${type.toString()}")
    
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("alarm_id", alarmId)
        putExtra("start_date", startDate.timeInMillis)
        putExtra("end_date", endDate.timeInMillis)
    }
    
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    
    val interval = when (type) {
        ScheduleType.MINUTE -> 60000L
        ScheduleType.FIFTEENMINUTE -> AlarmManager.INTERVAL_FIFTEEN_MINUTES
        ScheduleType.HOURLY -> AlarmManager.INTERVAL_HOUR
        else -> AlarmManager.INTERVAL_DAY
    }
    
    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        startDate.timeInMillis,
        interval,
        pendingIntent
    )
}