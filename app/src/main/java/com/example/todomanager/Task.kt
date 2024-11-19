// Task.kt
package com.example.todomanager

data class Task(val name: String, val description: String, val dueDate: String) {
    override fun toString(): String {
        return "$name|$description|$dueDate"
    }

    companion object {
        fun fromString(taskString: String): Task? {
            val parts = taskString.split("|")
            return if (parts.size == 3) {
                Task(parts[0], parts[1], parts[2])
            } else null
        }
    }
}