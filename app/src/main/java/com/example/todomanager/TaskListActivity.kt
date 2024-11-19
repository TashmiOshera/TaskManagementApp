package com.example.todomanager

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TaskListActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var taskListView: ListView
    private lateinit var editSearchTask: EditText
    private lateinit var btnEditTask: Button
    private lateinit var timerButton: Button
    private lateinit var setReminder: Button
    private lateinit var btnDeleteTask: Button
    private var selectedTask: Task? = null
    private var taskList: MutableSet<String> = mutableSetOf()
    private lateinit var adapter: ArrayAdapter<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // Initialize UI components
        sharedPreferences = getSharedPreferences("TaskManagerPrefs", MODE_PRIVATE)
        timerButton = findViewById(R.id.stopwatch)
        editSearchTask = findViewById(R.id.edit_search_task)
        taskListView = findViewById(R.id.task_list_view)
        btnEditTask = findViewById(R.id.btn_edit_task)
        setReminder = findViewById(R.id.reminder_button)
        btnDeleteTask = findViewById(R.id.btn_delete_task)

        // Load task list and set up ListView
        loadTaskList()

        // Set up the item click listener for task selection
        taskListView.setOnItemClickListener { _, _, position, _ ->
            val taskString = taskList.elementAt(position)
            selectedTask = Task.fromString(taskString)  // Parse the string into a Task object
            selectedTask?.let {
                editSearchTask.setText(it.name)  // Display the selected task's name
            } ?: showToast("Error selecting task.")
        }

        // Set up button listeners
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        btnEditTask.setOnClickListener {
            selectedTask?.let { task ->
                editTask(task)
            } ?: showToast("Please select a task to edit.")
        }

        timerButton.setOnClickListener {
            selectedTask?.let { task ->
                val intent = Intent(this, TimerActivity::class.java).apply {
                    putExtra("task_name", task.name)
                }
                startActivity(intent)
            } ?: showToast("Please select a task before starting the timer.")
        }

        setReminder.setOnClickListener {
            selectedTask?.let { task ->
                val intent = Intent(this, ReminderActivity::class.java).apply {
                    putExtra("task_name", task.name)
                }
                startActivity(intent)
            } ?: showToast("Please select a task before setting a reminder.")
        }

        btnDeleteTask.setOnClickListener {
            selectedTask?.let { task ->
                deleteTask(task)
            } ?: showToast("Please select a task to delete.")
        }
    }

    private fun loadTaskList() {
        // Load tasks from SharedPreferences
        taskList = sharedPreferences.getStringSet("task_list", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList.toList())
        taskListView.adapter = adapter
    }

    private fun editTask(task: Task) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("task_data", task.toString())
            putExtra("is_editing", true)
        }
        startActivity(intent)
    }

    private fun deleteTask(task: Task) {
        val taskString = task.toString()
        if (taskList.remove(taskString)) {
            sharedPreferences.edit().putStringSet("task_list", taskList).apply()
            adapter.notifyDataSetChanged()  // Notify adapter of data changes
            showToast("Task deleted successfully!")
            selectedTask = null  // Reset selected task after deletion
            editSearchTask.text.clear() // Clear search/edit field
        } else {
            showToast("Task not found!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
