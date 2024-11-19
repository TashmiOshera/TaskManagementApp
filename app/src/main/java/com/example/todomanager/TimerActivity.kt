package com.example.todomanager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private var seconds = 0
    private lateinit var timerTextView: TextView
    private lateinit var taskNameTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            seconds++
            val minutes = seconds / 60
            val secs = seconds % 60
            timerTextView.text = String.format("%02d:%02d", minutes, secs)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timer_text_view)
        taskNameTextView = findViewById(R.id.task_name_text_view)
        startButton = findViewById(R.id.start_button)
        stopButton = findViewById(R.id.stop_button)
        resetButton = findViewById(R.id.reset_button)

        val taskName = intent.getStringExtra("task_name")
        taskNameTextView.text = taskName ?: "No Task Selected"

        startButton.setOnClickListener {
            handler.post(runnable)
        }

        stopButton.setOnClickListener {
            handler.removeCallbacks(runnable)
        }

        resetButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            seconds = 0
            timerTextView.text = "00:00"
        }
    }
}
