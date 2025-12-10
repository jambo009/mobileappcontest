package com.example.feeling_myapp.data

data class Student(
    val id: Int,
    val name: String,
    var mood: String,
    var note: String = ""
)

val initialStudentList = listOf(
    Student(1, "A", "❓"),
    Student(2, "B", "❓"),
    Student(3, "C", "❓"),
    Student(4, "D", "❓"),
    Student(5, "E", "❓"),
    Student(6, "F", "❓"),
    Student(7, "G", "❓"),
    Student(8, "H", "❓"),
    Student(9, "I", "❓"),
    Student(10, "J", "❓")
)

val MOOD_EMOJIS = listOf("😊", "😢", "😡", "😴")