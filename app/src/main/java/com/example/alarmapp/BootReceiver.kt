package com.example.alarmapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat


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
                showNotification(
                    context,
                    title = "Alarm Triggered!",
                    message = "This is you scheduled alarm!"
                )
            } else {
                // TODO REQUEST PERMISSION
            }
        } else {
            Log.d("AlarmReceiver", "Else Block Intent Action")
        }
    }
    
    
}
