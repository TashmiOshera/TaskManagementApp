package com.example.todomanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskName = intent.getStringExtra("task_name") ?: "Task"
        showNotification(context, taskName)
    }

    private fun showNotification(context: Context, taskName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent to open TaskListActivity when notification is tapped
        val intent = Intent(context, TaskListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        val notification = NotificationCompat.Builder(context, "task_reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)  // Replace with your notification icon
            .setContentTitle("Task Reminder")
            .setContentText("It's time for: $taskName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000))  // Vibration pattern
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)  // Default sound
            .setLights(Color.RED, 1000, 300)  // Set LED light color, on for 1 second, off for 300 milliseconds
            .build()

        // Notify the user
        notificationManager.notify(0, notification)
    }
}
