package com.example.feeling_myapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feeling_myapp.R
import com.example.feeling_myapp.data.MOOD_EMOJIS
import com.example.feeling_myapp.data.Student
import com.example.feeling_myapp.data.initialStudentList


@Composable
fun StudentMoodRecordApp() {
    var isSessionActive by remember { mutableStateOf(false) }
    val students = remember { initialStudentList.toMutableStateList() }

    if (isSessionActive) {
        MainMoodGrid(
            students = students,
            onStopSession = {
                isSessionActive = false
                students.clear()
                students.addAll(initialStudentList)
            }
        )
    } else {
        SessionStartScreen(
            onStartSession = { isSessionActive = true }
        )
    }
}

@Composable
fun SessionStartScreen(onStartSession: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.chalkboard_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(Color.Black.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .border(4.dp, Color(0xFF5E8B7E), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("😊 😢\n😡 😴", fontSize = MaterialTheme.typography.headlineLarge.fontSize, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "학생 기분 기록 앱",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = onStartSession,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C5B8C))
            ) {
                Text("시작", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.chimp),
                    contentDescription = "시작",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "버튼을 눌러 기분 기록을 시작합니다.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMoodGrid(students: SnapshotStateList<Student>, onStopSession: () -> Unit) {
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("우리 반 기분 현황") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    Button(onClick = onStopSession, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
                        Text("종료")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = "학생을 선택하여 기분과 이유를 기록하세요.",
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(students) { student ->
                    StudentMoodCard(student) {
                        selectedStudent = student
                        showDialog = true
                    }
                }
            }
        }
    }

    if (showDialog && selectedStudent != null) {
        MoodSelectionDialog(
            student = selectedStudent!!,
            onDismiss = { showDialog = false },
            onMoodSelected = { newMood, newNote ->
                val index = students.indexOfFirst { it.id == selectedStudent!!.id }
                if (index != -1) {
                    students[index] = students[index].copy(mood = newMood, note = newNote)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun StudentMoodCard(student: Student, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(student.mood, fontSize = MaterialTheme.typography.headlineLarge.fontSize, modifier = Modifier.padding(bottom = 4.dp))
        Text(student.name, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)

        if (student.note.isNotBlank()) {
            Text("📝 메모됨", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun MoodSelectionDialog(student: Student, onDismiss: () -> Unit, onMoodSelected: (String, String) -> Unit) {
    var selectedMood by remember { mutableStateOf(student.mood) }
    var currentNote by remember { mutableStateOf(student.note) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("${student.name}, 지금 기분이 어떤가요?") },
        text = {
            Column {
                Text("1. 기분을 선택해주세요")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MOOD_EMOJIS.forEach { mood ->
                        Button(
                            onClick = { selectedMood = mood },
                            modifier = Modifier.weight(1f).height(48.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = if (selectedMood == mood) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) else ButtonDefaults.buttonColors()
                        ) { Text(mood, fontSize = MaterialTheme.typography.headlineMedium.fontSize) }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("2. 이유를 간단히 적어주세요")
                OutlinedTextField(
                    value = currentNote,
                    onValueChange = { currentNote = it },
                    label = { Text("예: 칭찬받아서 신남") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onMoodSelected("❓", "") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) { Text("기록 초기화", color = MaterialTheme.colorScheme.onErrorContainer) }
            }
        },
        confirmButton = {
            Button(
                onClick = { onMoodSelected(selectedMood, currentNote) },
                enabled = selectedMood != "❓" && selectedMood.isNotBlank()
            ) { Text("저장") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("취소") } }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() { ComposeLabTheme { StudentMoodRecordApp() } }