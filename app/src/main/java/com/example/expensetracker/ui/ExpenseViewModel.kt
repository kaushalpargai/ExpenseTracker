package com.example.expensetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.ai.ExpenseAnalysis
import com.example.expensetracker.ai.GeminiService
import com.example.expensetracker.data.CategorySummary
import com.example.expensetracker.data.Expense
import com.example.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class ExpenseViewModel(
    private val repository: ExpenseRepository,
    private val geminiService: GeminiService?
) : ViewModel() {

    val expenses = repository.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalExpenses = repository.totalExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Category summaries with calculated percentages
    val categorySummaries = repository.categorySummaries
        .combine(totalExpenses) { summaries, total ->
            summaries.map { summary ->
                summary.copy(
                    percentage = if (total != null && total > 0) {
                        (summary.totalAmount / total) * 100
                    } else {
                        0.0
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val weeklyExpenses = repository.getWeeklyExpenses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val monthlyExpenses = repository.getMonthlyExpenses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // AI Analysis state
    private val _aiAnalysis = MutableStateFlow<ExpenseAnalysis?>(null)
    val aiAnalysis: StateFlow<ExpenseAnalysis?> = _aiAnalysis.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisError = MutableStateFlow<String?>(null)
    val analysisError: StateFlow<String?> = _analysisError.asStateFlow()

    fun addExpense(amount: Double, description: String, category: String) {
        viewModelScope.launch {
            val expense = Expense(
                amount = amount,
                description = description,
                category = category,
                date = Date()
            )
            repository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun requestAIAnalysis() {
        if (geminiService == null) {
            _analysisError.value = "AI service not configured. Please add your Gemini API key."
            return
        }

        viewModelScope.launch {
            try {
                _isAnalyzing.value = true
                _analysisError.value = null

                val weekly = weeklyExpenses.value
                val monthly = monthlyExpenses.value
                val categories = categorySummaries.value

                if (monthly.isEmpty()) {
                    _analysisError.value = "Not enough expense data. Add some expenses first!"
                    _isAnalyzing.value = false
                    return@launch
                }

                val analysis = geminiService.analyzeExpenses(weekly, monthly, categories)
                _aiAnalysis.value = analysis
            } catch (e: Exception) {
                _analysisError.value = "Failed to analyze expenses: ${e.message}"
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun clearAnalysisError() {
        _analysisError.value = null
    }

    class Factory(
        private val repository: ExpenseRepository,
        private val geminiService: GeminiService?
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                return ExpenseViewModel(repository, geminiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
