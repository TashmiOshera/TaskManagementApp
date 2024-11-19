package com.example.todomanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LaunchScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
    }

    // Called when the user taps the "Get Started" button
    fun onStartButtonClick(view: View) {
        Toast.makeText(this, "Welcome to the app!", Toast.LENGTH_SHORT).show()

        // Navigate to TaskListActivity or Main Screen
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }
}
