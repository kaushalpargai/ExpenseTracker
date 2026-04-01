package com.example.expensetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenses(): Flow<Double?>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): Expense?

    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("""
        SELECT category, SUM(amount) as totalAmount, COUNT(*) as expenseCount, 0.0 as percentage
        FROM expenses 
        GROUP BY category 
        ORDER BY totalAmount DESC
    """)
    fun getCategorySummaries(): Flow<List<CategorySummary>>

    @Query("""
        SELECT * FROM expenses 
        WHERE date >= :startDate AND date <= :endDate 
        ORDER BY date DESC
    """)
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: String): Flow<List<Expense>>

    @Query("""
        SELECT * FROM expenses 
        WHERE date >= :startDate 
        ORDER BY date DESC
    """)
    fun getExpensesSinceDate(startDate: Long): Flow<List<Expense>>

    @Query("""
        SELECT SUM(amount) FROM expenses 
        WHERE date >= :startDate AND date <= :endDate
    """)
    fun getTotalExpensesByDateRange(startDate: Long, endDate: Long): Flow<Double?>
}
