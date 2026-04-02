package com.example.expensetracker.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    val totalExpenses: Flow<Double?> = expenseDao.getTotalExpenses()

    suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun getExpenseById(id: Long): Expense? {
        return expenseDao.getExpenseById(id)
    }

    // Category summaries
    val categorySummaries: Flow<List<CategorySummary>> = expenseDao.getCategorySummaries()

    fun getExpensesByCategory(category: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category)
    }

    // Date range queries
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }

    fun getWeeklyExpenses(): Flow<List<Expense>> {
        val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        return expenseDao.getExpensesSinceDate(weekAgo)
    }

    fun getMonthlyExpenses(): Flow<List<Expense>> {
        val monthAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
        return expenseDao.getExpensesSinceDate(monthAgo)
    }

    fun getTotalExpensesByDateRange(startDate: Long, endDate: Long): Flow<Double?> {
        return expenseDao.getTotalExpensesByDateRange(startDate, endDate)
    }
}
