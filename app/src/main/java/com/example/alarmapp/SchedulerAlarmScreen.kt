package com.example.alarmapp

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.alarmapp.utils.showDatePicker
import java.util.Calendar
import android.Manifest
import android.annotation.SuppressLint
import android.widget.Space
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ScheduleAlarmScreen(context: Context, modifier: Modifier) {
    val calendar = Calendar.getInstance()
    var hour by remember { mutableIntStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }
    
    var startDate by remember { mutableStateOf(calendar) }
    var endDate by remember { mutableStateOf(calendar) }
    
    
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Schedule Alarm", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = modifier.height(16.dp))
        
        DateRangePicker(startDate = calendar, endDate = calendar) { start, end ->
            startDate = start
            endDate = end
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Row {
            Text(text = "Hour: ${hour}")
            Slider(
                value = hour.toFloat(),
                onValueChange = { time ->
                    hour = time.toInt()
                },
                valueRange = 0f..23f
            )
        }
        Row {
            Text(text = "Minute: $minute")
            Slider(
                value = minute.toFloat(),
                onValueChange = { minute = it.toInt() },
                valueRange = 0f..59f
            )
        }
        
        Button(
            onClick = {
                scheduleDailyAlarm(context, startDate, endDate, hour, minute)
            }
        ) {
            Text(text = "Set Alarm")
        }
        
        RequestNotificationPermission(context)
        
    }
}

@SuppressLint("InlinedApi")
@Composable
fun RequestNotificationPermission(context: Context) {
    
    val isPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
    
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showNotification(context, "Hello", "Test Notif")
        }
    }
    
    if (!isPermissionGranted) {
        Button(onClick = {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }) {
            Text(text = "Enable Notifications")
        }
    }
}


@Composable
fun DateRangePicker(
    startDate: Calendar?,
    endDate: Calendar?,
    onDateRangeSelected: (Calendar, Calendar) -> Unit
) {
    val context = LocalContext.current
    var start by remember { mutableStateOf(startDate) }
    var end by remember { mutableStateOf(endDate) }
    
    val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
    
    val formatStart = start?.time?.let {
        dateFormatter.format(it)
    }
    
    val formatEnd = end?.time?.let {
        dateFormatter.format(it)
    }
    
    Column {
        Text("Start date: ${formatStart ?: "Invalid date"}")
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            showDatePicker(context) { selectedDate ->
                start = selectedDate
                onDateRangeSelected(start!!, end!!)
            }
        }) {
            Text(text = "Change Date")
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text("End date: ${formatEnd ?: "Invalid date"}")
        Spacer(modifier = Modifier.height(5.dp))
        
        Button(onClick = {
            showDatePicker(context) { selectedDate ->
                end = selectedDate
                onDateRangeSelected(start!!, end!!)
            }
        }) {
            Text(text = "Change Date")
        }
        
    }
}
