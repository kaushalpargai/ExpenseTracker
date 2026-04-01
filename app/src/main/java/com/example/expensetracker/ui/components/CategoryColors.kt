package com.example.expensetracker.ui.components

import androidx.compose.ui.graphics.Color

/**
 * Returns the color associated with a category
 */
fun getCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "food" -> Color(0xFFFF6B9D)
        "transport" -> Color(0xFF4299E1)
        "shopping" -> Color(0xFFED8936)
        "entertainment" -> Color(0xFF9F7AEA)
        "bills" -> Color(0xFFECC94B)
        "healthcare" -> Color(0xFF48BB78)
        "education" -> Color(0xFF38B2AC)
        else -> Color(0xFF8B5CF6)
    }
}
