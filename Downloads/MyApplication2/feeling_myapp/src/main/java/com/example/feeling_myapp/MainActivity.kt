package com.example.feeling_myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.feeling_myapp.ui.theme.StudentMoodRecordApp
import com.example.feeling_myapp.ui.theme.ComposeLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLabTheme {
                StudentMoodRecordApp()
            }
        }
    }
}