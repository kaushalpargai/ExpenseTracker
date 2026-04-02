package com.example.expensetracker.ai

/**
 * Data class representing the result of AI expense analysis
 */
data class ExpenseAnalysis(
    val weeklyComparison: String,
    val monthlyComparison: String,
    val recommendations: List<String>,
    val insights: String,
    val timestamp: Long = System.currentTimeMillis()
)
