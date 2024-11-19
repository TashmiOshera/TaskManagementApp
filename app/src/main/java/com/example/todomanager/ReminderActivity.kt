package com.example.todomanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import java.util.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var taskNameEditText: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var setReminderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        taskNameEditText = findViewById(R.id.task_name_edit_text)
        timePicker = findViewById(R.id.time_picker)
        setReminderButton = findViewById(R.id.set_reminder_button)

        // Retrieve the task name from the intent
        val taskName = intent.getStringExtra("task_name")
        taskNameEditText.setText(taskName)  // Display the task name in the EditText

        setReminderButton.setOnClickListener {
            setReminder()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setReminder() {
        val taskName = taskNameEditText.text.toString()

        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour
        } else {
            timePicker.currentHour
        }
        val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.minute
        } else {
            timePicker.currentMinute
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("task_name", taskName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Log.d("ReminderActivity", "Alarm set for: ${calendar.timeInMillis} (current time: ${System.currentTimeMillis()})")

        Toast.makeText(this, "Reminder set for '$taskName' at ${String.format("%02d", hour)}:${String.format("%02d", minute)}", Toast.LENGTH_SHORT).show()
    }
}