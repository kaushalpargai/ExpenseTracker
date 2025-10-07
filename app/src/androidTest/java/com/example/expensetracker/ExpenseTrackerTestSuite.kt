package com.example.expensetracker

import com.example.expensetracker.data.ExpenseDatabaseTest
import com.example.expensetracker.ui.ExpenseViewModelTest
import com.example.expensetracker.ui.components.AddCategoryDialogTest
import com.example.expensetracker.ui.components.AddExpenseFormDialogTest
import com.example.expensetracker.ui.components.BottomNavigationTest
import com.example.expensetracker.ui.screens.DashboardScreenTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite that runs all instrumentation tests for the ExpenseTracker app.
 * 
 * This suite includes:
 * - UI Component Tests (Dashboard, Dialogs, Navigation)
 * - Database Integration Tests
 * - ViewModel Tests
 * - End-to-End Flow Tests
 * 
 * Run this suite to execute all tests at once:
 * ./gradlew connectedAndroidTest
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // UI Component Tests
    DashboardScreenTest::class,
    AddCategoryDialogTest::class,
    AddExpenseFormDialogTest::class,
    BottomNavigationTest::class,
    
    // Data Layer Tests
    ExpenseDatabaseTest::class,
    
    // ViewModel Tests
    ExpenseViewModelTest::class,
    
    // End-to-End Tests
    ExpenseTrackerE2ETest::class
)
class ExpenseTrackerTestSuite
