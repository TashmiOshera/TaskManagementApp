package com.example.todomanager

//import ReminderActivity

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var timerButton: Button
    private lateinit var addTaskButton: Button
    private lateinit var reminderButton: Button
    private lateinit var searchBar: EditText
    private lateinit var upcomingTasksBar: TextView
    private lateinit var viewTasksButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("ProductivityAppPrefs", MODE_PRIVATE)

        // Initialize UI elements
        timerButton = findViewById(R.id.timer_button)
        viewTasksButton = findViewById(R.id.view_tasks_button)
        addTaskButton = findViewById(R.id.add_task_button)
        reminderButton = findViewById(R.id.reminder_button)
        upcomingTasksBar = findViewById(R.id.upcoming_tasks_bar)


        // Display welcome message
        showWelcomeMessage()

        // Load task data
        loadTaskList()

        // Set up click listeners for buttons
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        viewTasksButton.setOnClickListener {
            Log.d("HomePage", "View Tasks button clicked")
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }


        addTaskButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Optionally, you could add a task here and then update the widget
            updateWidget()
        }

        timerButton.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        reminderButton.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadTaskList() {
        val taskList = sharedPreferences.getStringSet("task_list", setOf()) ?: setOf()
        // Update widget after loading tasks
        updateWidget()
        if (taskList.isEmpty()) {
            upcomingTasksBar.text = "Upcoming Tasks: None"
        } else {
            val formattedTaskList = taskList.joinToString(separator = "\n") { it }
            upcomingTasksBar.text = "Upcoming Tasks:\n$formattedTaskList"
        }
    }

    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, TaskListWidget::class.java))
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_text_view)
    }

    // Show a welcome message when the user opens the app
    private fun showWelcomeMessage() {
        val userName = sharedPreferences.getString("user_name", "User") ?: "User"
        Toast.makeText(this, "Welcome back, $userName!", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh task list when returning to HomePage
        loadTaskList()
    }
}