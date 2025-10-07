package com.example.expensetracker.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.expensetracker.data.Expense
import com.example.expensetracker.data.ExpenseRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ExpenseViewModelTest {

    private lateinit var mockRepository: ExpenseRepository
    private lateinit var viewModel: ExpenseViewModel

    private val testExpenses = listOf(
        Expense(1, 100.0, "Test Expense 1", "Food", Date()),
        Expense(2, 200.0, "Test Expense 2", "Transport", Date())
    )

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        
        // Setup mock flows
        coEvery { mockRepository.allExpenses } returns MutableStateFlow(testExpenses)
        coEvery { mockRepository.totalExpenses } returns MutableStateFlow(300.0)
        
        viewModel = ExpenseViewModel(mockRepository)
    }

    @Test
    fun viewModel_expensesFlow_returnsRepositoryData() = runTest {
        // Get expenses from ViewModel
        val expenses = viewModel.expenses.first()

        // Verify it matches repository data
        assertEquals(2, expenses.size)
        assertEquals("Test Expense 1", expenses[0].description)
        assertEquals("Test Expense 2", expenses[1].description)
    }

    @Test
    fun viewModel_totalExpensesFlow_returnsRepositoryData() = runTest {
        // Get total expenses from ViewModel
        val total = viewModel.totalExpenses.first()

        // Verify it matches repository data
        assertEquals(300.0, total)
    }

    @Test
    fun addExpense_callsRepositoryInsert() = runTest {
        // Add expense through ViewModel
        viewModel.addExpense(150.0, "New Expense", "Entertainment")

        // Verify repository insert was called
        coVerify {
            mockRepository.insertExpense(
                match { expense ->
                    expense.amount == 150.0 &&
                    expense.description == "New Expense" &&
                    expense.category == "Entertainment"
                }
            )
        }
    }

    @Test
    fun deleteExpense_callsRepositoryDelete() = runTest {
        val expenseToDelete = testExpenses[0]

        // Delete expense through ViewModel
        viewModel.deleteExpense(expenseToDelete)

        // Verify repository delete was called
        coVerify { mockRepository.deleteExpense(expenseToDelete) }
    }

    @Test
    fun addExpense_withValidData_createsExpenseWithCurrentDate() = runTest {
        val beforeTime = System.currentTimeMillis()
        
        // Add expense
        viewModel.addExpense(75.50, "Coffee", "Food")
        
        val afterTime = System.currentTimeMillis()

        // Verify expense was created with current date
        coVerify {
            mockRepository.insertExpense(
                match { expense ->
                    expense.amount == 75.50 &&
                    expense.description == "Coffee" &&
                    expense.category == "Food" &&
                    expense.date.time >= beforeTime &&
                    expense.date.time <= afterTime
                }
            )
        }
    }

    @Test
    fun addExpense_multipleExpenses_callsRepositoryMultipleTimes() = runTest {
        // Add multiple expenses
        viewModel.addExpense(50.0, "Expense 1", "Food")
        viewModel.addExpense(100.0, "Expense 2", "Transport")
        viewModel.addExpense(25.0, "Expense 3", "Entertainment")

        // Verify repository was called 3 times
        coVerify(exactly = 3) { mockRepository.insertExpense(any()) }
    }

    @Test
    fun deleteExpense_multipleExpenses_callsRepositoryCorrectly() = runTest {
        // Delete multiple expenses
        viewModel.deleteExpense(testExpenses[0])
        viewModel.deleteExpense(testExpenses[1])

        // Verify repository was called for each expense
        coVerify { mockRepository.deleteExpense(testExpenses[0]) }
        coVerify { mockRepository.deleteExpense(testExpenses[1]) }
    }

    @Test
    fun viewModel_initialState_hasCorrectValues() = runTest {
        // Create new ViewModel with empty repository
        val emptyRepository = mockk<ExpenseRepository>(relaxed = true)
        coEvery { emptyRepository.allExpenses } returns MutableStateFlow(emptyList())
        coEvery { emptyRepository.totalExpenses } returns MutableStateFlow(0.0)
        
        val newViewModel = ExpenseViewModel(emptyRepository)

        // Verify initial state
        val expenses = newViewModel.expenses.first()
        val total = newViewModel.totalExpenses.first()

        assertEquals(0, expenses.size)
        assertEquals(0.0, total)
    }

    @Test
    fun addExpense_withZeroAmount_stillCallsRepository() = runTest {
        // Add expense with zero amount
        viewModel.addExpense(0.0, "Free sample", "Food")

        // Verify repository was still called (validation should be in UI layer)
        coVerify {
            mockRepository.insertExpense(
                match { expense ->
                    expense.amount == 0.0 &&
                    expense.description == "Free sample"
                }
            )
        }
    }

    @Test
    fun addExpense_withEmptyDescription_stillCallsRepository() = runTest {
        // Add expense with empty description
        viewModel.addExpense(50.0, "", "Food")

        // Verify repository was still called (validation should be in UI layer)
        coVerify {
            mockRepository.insertExpense(
                match { expense ->
                    expense.amount == 50.0 &&
                    expense.description == ""
                }
            )
        }
    }
}
