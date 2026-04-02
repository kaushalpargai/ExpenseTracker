package com.example.expensetracker.data

/**
 * Data class representing a summary of expenses for a specific category
 * Used for displaying grouped expense data in the UI
 */
data class CategorySummary(
    val category: String,
    val totalAmount: Double,
    val expenseCount: Int,
    val percentage: Double = 0.0
)
