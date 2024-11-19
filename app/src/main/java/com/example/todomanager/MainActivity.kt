// MainActivity.kt
package com.example.todomanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isEditing: Boolean = false
    private var taskToEdit: Task? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("TaskManagerPrefs", Context.MODE_PRIVATE)

        val taskNameEditText: EditText = findViewById(R.id.task_name)
        val taskDescEditText: EditText = findViewById(R.id.task_desc)
        val taskDueDateEditText: EditText = findViewById(R.id.task_due_date)
        val btnSave: Button = findViewById(R.id.btn_save)
        val btnViewTasks: Button = findViewById(R.id.btn_view_tasks)

        // Check for editing task
        intent?.let {
            if (it.getBooleanExtra("is_editing", false)) {
                taskToEdit = Task.fromString(it.getStringExtra("task_data")!!)
                taskNameEditText.setText(taskToEdit?.name)
                taskDescEditText.setText(taskToEdit?.description)
                taskDueDateEditText.setText(taskToEdit?.dueDate)
                isEditing = true
                btnSave.text = "Update Task"
            }
        }

        btnSave.setOnClickListener {
            saveTaskData(taskNameEditText, taskDescEditText, taskDueDateEditText)
        }

        btnViewTasks.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveTaskData(taskNameEditText: EditText, taskDescEditText: EditText, taskDueDateEditText: EditText) {
        val taskName = taskNameEditText.text.toString()
        val taskDesc = taskDescEditText.text.toString()
        val taskDueDate = taskDueDateEditText.text.toString()

        if (taskName.isNotEmpty() && taskDesc.isNotEmpty() && taskDueDate.isNotEmpty()) {
            val taskList = sharedPreferences.getStringSet("task_list", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            if (isEditing && taskToEdit != null) {
                taskList.remove(taskToEdit.toString()) // Remove the old task
                isEditing = false
            }

            taskList.add(Task(taskName, taskDesc, taskDueDate).toString())

            with(sharedPreferences.edit()) {
                putStringSet("task_list", taskList)
                apply()
            }

            Toast.makeText(this, if (isEditing) "Task updated successfully!" else "Task saved successfully!", Toast.LENGTH_SHORT).show()
            taskNameEditText.text.clear()
            taskDescEditText.text.clear()
            taskDueDateEditText.text.clear()
            taskToEdit = null
            val btnSave: Button = findViewById(R.id.btn_save)

        } else {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show()
        }
    }
}