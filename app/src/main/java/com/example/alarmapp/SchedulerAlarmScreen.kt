package com.example.alarmapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.alarmapp.utils.ScheduleType
import com.example.alarmapp.utils.showDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAlarmScreen(context: Context, modifier: Modifier) {
    
    val scheduleTypes = listOf<Map<String, Any>>(
        mapOf(
            "title" to "Minute",
            "value" to ScheduleType.MINUTE
        ),
        mapOf(
            "title" to "Fifteen",
            "value" to ScheduleType.FIFTEENMINUTE
        ),
        mapOf(
            "title" to "Hour",
            "value" to ScheduleType.HOURLY
        ),
        mapOf(
            "title" to "Daily",
            "value" to ScheduleType.DALY
        )
    )
    
    val calendar = Calendar.getInstance()
    
    var startDate by remember { mutableStateOf(calendar) }
    var endDate by remember { mutableStateOf(calendar) }
    
    var selectedTime by remember { mutableStateOf<TimePickerState?>(null) }
    
    val selectedSchedule = remember {
        mutableStateOf(ScheduleType.DALY)
    }
    
    val showDialog = remember {
        mutableStateOf(false)
    }
    
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Schedule Alarm", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = modifier
                .padding(vertical = 4.dp)
        ) {
            Text(
                "Type: ",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Box(modifier = Modifier.width(10.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                items(scheduleTypes) { item ->
                    AssistChip(
                        onClick = {
                            selectedSchedule.value = item["value"] as ScheduleType
                        },
                        label = { Text(item["title"] as String) },
                        leadingIcon = {
                            if (selectedSchedule.value == item["value"])
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Schedule",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 5.dp),
                    )
                }
            }
        }
        
        
        DateRangePicker(startDate = calendar, endDate = calendar) { start, end ->
            startDate = start
            endDate = end
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        when {
            selectedTime != null -> {
                Text("Alarm set: ${selectedTime?.hour}:${selectedTime?.minute} WIB")
            }
            
            else -> {
                Button(
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    Text(text = "Set Clock")
                }
            }
        }
        
        when {
            showDialog.value -> {
                TimePickerCompose(
                    onDismiss = {
                        showDialog.value = false
                    },
                    onConfirm = { it ->
                        selectedTime = it
                        
                        selectedTime?.let {
                            Log.d("Set Clock Hour", it.hour.toString())
                            Log.d("Set Clock Minute", it.minute.toString())
                        }
                        
                        showDialog.value = false
                        
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            enabled = selectedTime != null,
            onClick = {
                scheduleDailyAlarm(
                    context,
                    startDate,
                    endDate,
                    selectedTime!!.hour,
                    selectedTime!!.minute,
                    selectedSchedule.value,
                )
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
