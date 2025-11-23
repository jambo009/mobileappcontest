package com.example.feeling_myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feeling_myapp.ui.theme.ComposeLabTheme // <<<-- 이 부분이 수정됨

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLabTheme { // <<<-- 이 부분이 수정됨
                StudentMoodScreen()
            }
        }
    }
}

data class Student(val id: Int, val name: String, var mood: String)

val studentList = listOf(
    Student(1, "일현", "❓"),
    Student(2, "이현", "❓"),
    Student(3, "삼현", "❓"),
    Student(4, "사현", "❓"),
    Student(5, "오현", "❓"),
    Student(6, "육현", "❓"),
    Student(7, "칠현", "❓"),
    Student(8, "팔현", "❓"),
    Student(9, "구현", "❓"),
    Student(10, "십현", "❓")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMoodScreen() {
    val students = remember { studentList.toMutableStateList() }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("학생 기분 기록") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "학생을 선택하여 기분을 기록하세요.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(students) { student ->
                    StudentItem(student) {
                        selectedStudent = student
                        showDialog = true
                    }
                }
            }
        }
    }

    if (showDialog && selectedStudent != null) {
        MoodSelectionDialog(
            studentName = selectedStudent!!.name,
            onDismiss = { showDialog = false },
            onMoodSelected = { newMood ->
                val index = students.indexOfFirst { it.id == selectedStudent!!.id }
                if (index != -1) {
                    students[index] = students[index].copy(mood = newMood)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun StudentItem(student: Student, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
            .background(Color(0xFFF0F0F0), MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = student.mood,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = student.name,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
fun MoodSelectionDialog(studentName: String, onDismiss: () -> Unit, onMoodSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("$studentName, 지금 기분이 어때?") },
        text = {
            Column {
                Text("오늘 기분을 선택하세요")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MoodButton("😊", onMoodSelected, Modifier.weight(1f))
                    MoodButton("😢", onMoodSelected, Modifier.weight(1f))
                    MoodButton("😡", onMoodSelected, Modifier.weight(1f))
                    MoodButton("😴", onMoodSelected, Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("닫기")
            }
        }
    )
}

@Composable
fun MoodButton(mood: String, onMoodSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = { onMoodSelected(mood) },
        modifier = modifier.height(48.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(mood, fontSize = MaterialTheme.typography.headlineMedium.fontSize)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStudentMoodScreen() {
    ComposeLabTheme { // <<<-- 이 부분이 수정됨
        StudentMoodScreen()
    }
}