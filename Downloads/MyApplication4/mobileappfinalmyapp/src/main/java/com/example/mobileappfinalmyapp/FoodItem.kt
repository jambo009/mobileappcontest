package com.example.mobileappfinalmyapp

import androidx.compose.ui.graphics.Color

data class FoodItem(
    val id: Int,
    val name: String,
    val expiryTimestamp: Long,
    val category: String = "냉장"
) {
    val daysLeft: Long
        get() {
            val diff = expiryTimestamp - System.currentTimeMillis()
            return diff / (24 * 60 * 60 * 1000)
        }

    val statusColor: Color
        get() = when {
            daysLeft < 0 -> Color(0xFFD32F2F)
            daysLeft <= 3 -> Color(0xFFFF5252)
            daysLeft <= 7 -> Color(0xFFFFC107)
            else -> Color(0xFF4CAF50)
        }
}