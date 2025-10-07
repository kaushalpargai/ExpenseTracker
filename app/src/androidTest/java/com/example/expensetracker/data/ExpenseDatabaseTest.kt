package com.example.expensetracker.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ExpenseDatabaseTest {

    private lateinit var database: ExpenseDatabase
    private lateinit var expenseDao: ExpenseDao

    @Before
    fun setup() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExpenseDatabase::class.java
        ).allowMainThreadQueries().build()
        
        expenseDao = database.expenseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertExpense_andRetrieve() = runBlocking {
        // Create test expense
        val expense = Expense(
            amount = 100.0,
            description = "Test Expense",
            category = "Food",
            date = Date()
        )

        // Insert expense
        expenseDao.insertExpense(expense)

        // Retrieve all expenses
        val expenses = expenseDao.getAllExpenses().first()

        // Verify expense was inserted
        assertEquals(1, expenses.size)
        assertEquals("Test Expense", expenses[0].description)
        assertEquals(100.0, expenses[0].amount)
        assertEquals("Food", expenses[0].category)
    }

    @Test
    fun insertMultipleExpenses_andRetrieve() = runBlocking {
        // Create test expenses
        val expense1 = Expense(
            amount = 50.0,
            description = "Lunch",
            category = "Food",
            date = Date()
        )
        
        val expense2 = Expense(
            amount = 25.0,
            description = "Bus fare",
            category = "Transport",
            date = Date()
        )

        // Insert expenses
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)

        // Retrieve all expenses
        val expenses = expenseDao.getAllExpenses().first()

        // Verify both expenses were inserted
        assertEquals(2, expenses.size)
        assertTrue(expenses.any { it.description == "Lunch" })
        assertTrue(expenses.any { it.description == "Bus fare" })
    }

    @Test
    fun deleteExpense_removesFromDatabase() = runBlocking {
        // Create and insert test expense
        val expense = Expense(
            amount = 75.0,
            description = "Coffee",
            category = "Food",
            date = Date()
        )
        
        expenseDao.insertExpense(expense)
        
        // Verify expense exists
        var expenses = expenseDao.getAllExpenses().first()
        assertEquals(1, expenses.size)
        
        // Delete the expense
        expenseDao.deleteExpense(expenses[0])
        
        // Verify expense was deleted
        expenses = expenseDao.getAllExpenses().first()
        assertEquals(0, expenses.size)
    }

    @Test
    fun getTotalExpenses_calculatesCorrectly() = runBlocking {
        // Insert multiple expenses
        expenseDao.insertExpense(Expense(0, 100.0, "Expense 1", "Food", Date()))
        expenseDao.insertExpense(Expense(0, 50.0, "Expense 2", "Transport", Date()))
        expenseDao.insertExpense(Expense(0, 25.0, "Expense 3", "Entertainment", Date()))

        // Get total expenses
        val total = expenseDao.getTotalExpenses().first()

        // Verify total is calculated correctly
        assertEquals(175.0, total ?: 0.0)
    }

    @Test
    fun getTotalExpenses_withNoExpenses_returnsNull() = runBlocking {
        // Get total expenses when database is empty
        val total = expenseDao.getTotalExpenses().first()

        // Verify total is null for empty database
        assertEquals(null, total)
    }

    @Test
    fun expenseAutoGeneration_worksCorrectly() = runBlocking {
        // Create expenses without specifying ID
        val expense1 = Expense(
            amount = 100.0,
            description = "First Expense",
            category = "Food",
            date = Date()
        )
        
        val expense2 = Expense(
            amount = 200.0,
            description = "Second Expense",
            category = "Transport",
            date = Date()
        )

        // Insert expenses
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)

        // Retrieve expenses
        val expenses = expenseDao.getAllExpenses().first()

        // Verify IDs were auto-generated and are different
        assertEquals(2, expenses.size)
        assertTrue(expenses[0].id != expenses[1].id)
        assertTrue(expenses[0].id > 0)
        assertTrue(expenses[1].id > 0)
    }

    @Test
    fun expenseFlow_updatesAutomatically() = runBlocking {
        // Get initial expenses (should be empty)
        var expenses = expenseDao.getAllExpenses().first()
        assertEquals(0, expenses.size)

        // Insert an expense
        val expense = Expense(
            amount = 50.0,
            description = "Test Flow",
            category = "Test",
            date = Date()
        )
        expenseDao.insertExpense(expense)

        // Get updated expenses
        expenses = expenseDao.getAllExpenses().first()
        assertEquals(1, expenses.size)
        assertEquals("Test Flow", expenses[0].description)
    }

    @Test
    fun totalExpensesFlow_updatesAutomatically() = runBlocking {
        // Get initial total (should be null)
        var total = expenseDao.getTotalExpenses().first()
        assertEquals(null, total)

        // Insert an expense
        expenseDao.insertExpense(Expense(0, 100.0, "Test", "Food", Date()))

        // Get updated total
        total = expenseDao.getTotalExpenses().first()
        assertEquals(100.0, total)

        // Insert another expense
        expenseDao.insertExpense(Expense(0, 50.0, "Test 2", "Transport", Date()))

        // Get updated total
        total = expenseDao.getTotalExpenses().first()
        assertEquals(150.0, total)
    }

    @Test
    fun expenseWithSpecialCharacters_handledCorrectly() = runBlocking {
        // Create expense with special characters
        val expense = Expense(
            amount = 99.99,
            description = "Café & Restaurant - 50% off!",
            category = "Food & Drinks",
            date = Date()
        )

        // Insert and retrieve
        expenseDao.insertExpense(expense)
        val expenses = expenseDao.getAllExpenses().first()

        // Verify special characters are preserved
        assertEquals(1, expenses.size)
        assertEquals("Café & Restaurant - 50% off!", expenses[0].description)
        assertEquals("Food & Drinks", expenses[0].category)
    }
}
