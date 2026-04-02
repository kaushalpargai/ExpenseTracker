package com.example.expensetracker.ai

import com.example.expensetracker.data.CategorySummary
import com.example.expensetracker.data.Expense
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Service for integrating with Google Gemini AI to analyze expenses
 */
class GeminiService(private val apiKey: String) {

    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey,
        generationConfig = generationConfig {
            temperature = 0.7f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 1024
        }
    )

    /**
     * Analyzes expenses and provides insights and recommendations
     */
    suspend fun analyzeExpenses(
        weeklyExpenses: List<Expense>,
        monthlyExpenses: List<Expense>,
        categorySummaries: List<CategorySummary>
    ): ExpenseAnalysis = withContext(Dispatchers.IO) {
        try {
            val prompt = buildAnalysisPrompt(weeklyExpenses, monthlyExpenses, categorySummaries)
            val response = model.generateContent(prompt)
            parseAnalysisResponse(response.text ?: "No analysis available")
        } catch (e: Exception) {
            // Return error analysis
            ExpenseAnalysis(
                weeklyComparison = "Unable to analyze weekly expenses",
                monthlyComparison = "Unable to analyze monthly expenses",
                recommendations = listOf("Error: ${e.message}"),
                insights = "Please check your internet connection and API key"
            )
        }
    }

    private fun buildAnalysisPrompt(
        weeklyExpenses: List<Expense>,
        monthlyExpenses: List<Expense>,
        categorySummaries: List<CategorySummary>
    ): String {
        val weeklyTotal = weeklyExpenses.sumOf { it.amount }
        val monthlyTotal = monthlyExpenses.sumOf { it.amount }
        val weeklyAverage = if (weeklyExpenses.isNotEmpty()) weeklyTotal / 7 else 0.0
        val monthlyAverage = if (monthlyExpenses.isNotEmpty()) monthlyTotal / 30 else 0.0

        val categoryBreakdown = categorySummaries.joinToString("\n") {
            "- ${it.category}: ₹${String.format("%.2f", it.totalAmount)} (${it.expenseCount} expenses)"
        }

        return """
            You are a personal finance advisor. Analyze the following expense data and provide insights.
            
            WEEKLY EXPENSES (Last 7 days):
            - Total: ₹${String.format("%.2f", weeklyTotal)}
            - Daily Average: ₹${String.format("%.2f", weeklyAverage)}
            - Number of transactions: ${weeklyExpenses.size}
            
            MONTHLY EXPENSES (Last 30 days):
            - Total: ₹${String.format("%.2f", monthlyTotal)}
            - Daily Average: ₹${String.format("%.2f", monthlyAverage)}
            - Number of transactions: ${monthlyExpenses.size}
            
            CATEGORY BREAKDOWN:
            $categoryBreakdown
            
            Please provide:
            1. WEEKLY_COMPARISON: Compare this week's spending to the monthly average (1-2 sentences)
            2. MONTHLY_COMPARISON: Analyze monthly spending patterns (1-2 sentences)
            3. RECOMMENDATIONS: Provide 3-5 specific, actionable recommendations to improve spending habits
            4. INSIGHTS: Share 1-2 key insights about spending patterns
            
            Format your response EXACTLY as follows:
            WEEKLY_COMPARISON: [your analysis]
            MONTHLY_COMPARISON: [your analysis]
            RECOMMENDATIONS:
            - [recommendation 1]
            - [recommendation 2]
            - [recommendation 3]
            INSIGHTS: [your insights]
        """.trimIndent()
    }

    private fun parseAnalysisResponse(responseText: String): ExpenseAnalysis {
        val lines = responseText.lines()
        var weeklyComparison = ""
        var monthlyComparison = ""
        val recommendations = mutableListOf<String>()
        var insights = ""
        
        var currentSection = ""
        
        for (line in lines) {
            when {
                line.startsWith("WEEKLY_COMPARISON:") -> {
                    weeklyComparison = line.substringAfter("WEEKLY_COMPARISON:").trim()
                    currentSection = "weekly"
                }
                line.startsWith("MONTHLY_COMPARISON:") -> {
                    monthlyComparison = line.substringAfter("MONTHLY_COMPARISON:").trim()
                    currentSection = "monthly"
                }
                line.startsWith("RECOMMENDATIONS:") -> {
                    currentSection = "recommendations"
                }
                line.startsWith("INSIGHTS:") -> {
                    insights = line.substringAfter("INSIGHTS:").trim()
                    currentSection = "insights"
                }
                line.trim().startsWith("-") && currentSection == "recommendations" -> {
                    recommendations.add(line.trim().removePrefix("-").trim())
                }
                line.isNotBlank() -> {
                    when (currentSection) {
                        "weekly" -> weeklyComparison += " " + line.trim()
                        "monthly" -> monthlyComparison += " " + line.trim()
                        "insights" -> insights += " " + line.trim()
                    }
                }
            }
        }

        return ExpenseAnalysis(
            weeklyComparison = weeklyComparison.ifBlank { "No weekly comparison available" },
            monthlyComparison = monthlyComparison.ifBlank { "No monthly comparison available" },
            recommendations = recommendations.ifEmpty { listOf("No recommendations available") },
            insights = insights.ifBlank { "No insights available" }
        )
    }
}
