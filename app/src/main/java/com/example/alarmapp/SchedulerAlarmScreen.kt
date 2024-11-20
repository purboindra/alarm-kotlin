package com.example.alarmapp

import android.content.Context
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
import com.example.alarmapp.utils.showDatePicker
import java.util.Calendar

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

//        DateRangePicker(startDate = calendar, endDate = calendar) { startDate, endDate ->
//
//
//        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text(text = "Hour: ${hour}")
            Slider(
                value = hour.toFloat(),
                onValueChange = { hour = it.toInt() },
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
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                scheduleDailyAlarm(context, startDate, endDate, hour, minute)
            }
        ) {
            Text(text = "Set Alarm")
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

    Column {
        Button(onClick = {
            showDatePicker(context) { selectedDate ->
                start = selectedDate
            }
        }) {
            Text(text = start?.time.toString())
        }

        Button(onClick = {
            showDatePicker(context) { selectedDate ->
                end = selectedDate
            }
        }) {
            Text(text = end?.time.toString() ?: "Select End Date")
        }

        if (start != null && end != null) {
            Button(onClick = { onDateRangeSelected(start!!, end!!) }) {
                Text("Confirm Date Range")
            }
        }
    }
}
