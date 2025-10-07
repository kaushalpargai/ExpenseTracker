package com.example.expensetracker.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.expensetracker.data.Expense
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: ExpenseViewModel
    private lateinit var mockExpenses: MutableStateFlow<List<Expense>>
    private lateinit var mockTotalExpenses: MutableStateFlow<Double?>

    @Before
    fun setup() {
        mockViewModel = mockk(relaxed = true)
        mockExpenses = MutableStateFlow(emptyList())
        mockTotalExpenses = MutableStateFlow(0.0)

        every { mockViewModel.expenses } returns mockExpenses
        every { mockViewModel.totalExpenses } returns mockTotalExpenses
    }

    @Test
    fun dashboardScreen_displaysCorrectly() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify main components are displayed
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        composeTestRule.onNodeWithText("$7,154.87").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.4%").assertIsDisplayed()
        composeTestRule.onNodeWithText("Month").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_displaysStatsCards() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify stats cards are displayed
        composeTestRule.onNodeWithText("Highest Expense").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lowest").assertIsDisplayed()
        composeTestRule.onNodeWithText("+$421.52").assertIsDisplayed()
        composeTestRule.onNodeWithText("+$741.00").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_displaysIncomeSection() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify income section is displayed
        composeTestRule.onNodeWithText("Incomes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Steady monthly other growth").assertIsDisplayed()
        composeTestRule.onNodeWithText("$7987.54").assertIsDisplayed()
        composeTestRule.onNodeWithText("Monthly salary").assertIsDisplayed()
        composeTestRule.onNodeWithText("75.5%").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_addButtonTriggersCallback() {
        var addButtonClicked = false

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = { addButtonClicked = true }
                )
            }
        }

        // Find and click the add button
        composeTestRule.onNodeWithContentDescription("Add").performClick()

        // Verify callback was triggered
        assert(addButtonClicked)
    }

    @Test
    fun dashboardScreen_profileAvatarIsDisplayed() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify profile avatar is displayed
        composeTestRule.onNodeWithContentDescription("Profile").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_settingsButtonIsDisplayed() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify settings button is displayed
        composeTestRule.onNodeWithContentDescription("Settings").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_scrollsCorrectly() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify we can scroll to see income section
        composeTestRule.onNodeWithText("Incomes").assertIsDisplayed()
        
        // Scroll up to verify header is still accessible
        composeTestRule.onRoot().performTouchInput {
            swipeUp()
        }
        
        // Header should still be visible after scroll
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_withExpenseData_displaysCorrectly() {
        // Setup mock data
        val testExpenses = listOf(
            Expense(1, 100.0, "Test Expense 1", "Food", Date()),
            Expense(2, 200.0, "Test Expense 2", "Transport", Date())
        )
        mockExpenses.value = testExpenses
        mockTotalExpenses.value = 300.0

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                DashboardScreen(
                    viewModel = mockViewModel,
                    onAddExpenseClick = {}
                )
            }
        }

        // Verify the screen still displays correctly with data
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        composeTestRule.onNodeWithText("Highest Expense").assertIsDisplayed()
        composeTestRule.onNodeWithText("Incomes").assertIsDisplayed()
    }
}
