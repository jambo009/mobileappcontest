package com.example.mobileappfinalmyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                MyFridgeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFridgeApp() {
    var isAppRunning by remember { mutableStateOf(false) }
    val oneDay = 24 * 60 * 60 * 1000L

    var foodList by remember {
        mutableStateOf(listOf(
            FoodItem(1, "우유", System.currentTimeMillis() + (2 * oneDay)),
            FoodItem(2, "계란", System.currentTimeMillis() + (10 * oneDay)),
            FoodItem(3, "닭가슴살", System.currentTimeMillis() + (5 * oneDay), "냉동"),
            FoodItem(4, "식빵", System.currentTimeMillis() - (1 * oneDay), "실온")
        ))
    }
    var showDialog by remember { mutableStateOf(false) }

    if (!isAppRunning) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFE3F2FD)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🧊 스마트 냉장고", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { isAppRunning = true },
                modifier = Modifier.height(56.dp).width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("관리 시작하기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("나의 냉장고", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = { isAppRunning = false }) {

                            Icon(Icons.Default.ExitToApp, contentDescription = "종료", tint = Color.Red)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showDialog = true }, containerColor = Color(0xFF2196F3)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF5F5F5))) {
                val urgentCount = foodList.count { it.daysLeft <= 3 }
                if (urgentCount > 0) {
                    Card(modifier = Modifier.padding(16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {

                        Text("⚠️ 유통기한 임박 식품: ${urgentCount}개", modifier = Modifier.padding(16.dp), color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(items = foodList, key = { it.id }) { item ->
                        FoodItemRow(item = item) { foodList = foodList.filter { it.id != item.id } }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddFoodDialog(onDismiss = { showDialog = false }, onAdd = { name, days ->
            val dayValue = days.toLongOrNull() ?: 7L
            foodList = foodList + FoodItem((foodList.maxOfOrNull { it.id } ?: 0) + 1, name, System.currentTimeMillis() + (dayValue * oneDay))
            showDialog = false
        })
    }
}