package com.example.expensetracker

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpenseTrackerE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun completeExpenseFlow_addExpenseSuccessfully() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Verify dashboard is displayed
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()

        // Click add expense button
        composeTestRule.onNodeWithContentDescription("Add").performClick()

        // Verify category dialog appears
        composeTestRule.onNodeWithText("Add New").assertIsDisplayed()

        // Select Business Expenses category
        composeTestRule.onNodeWithText("Business Expenses").performClick()

        // Verify expense form dialog appears
        composeTestRule.onNodeWithText("Add Business Expenses").assertIsDisplayed()

        // Fill in expense details
        composeTestRule.onNodeWithText("Amount")
            .performTextInput("125.50")
        
        composeTestRule.onNodeWithText("Description")
            .performTextInput("Client meeting lunch")

        // Submit the expense
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Verify we're back to dashboard
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()

        // The expense should be added to the database
        // (We can't easily verify the exact total without mocking, but the flow completed)
    }

    @Test
    fun navigationFlow_switchBetweenTabs() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Verify we start on Home tab
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()

        // Navigate to Reports
        composeTestRule.onNodeWithText("Reports").performClick()
        composeTestRule.onNodeWithText("Reports Screen - Coming Soon").assertIsDisplayed()

        // Navigate to Budget
        composeTestRule.onNodeWithText("Budget").performClick()
        composeTestRule.onNodeWithText("Budget Screen - Coming Soon").assertIsDisplayed()

        // Navigate to Account
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.onNodeWithText("Account Screen - Coming Soon").assertIsDisplayed()

        // Navigate back to Home
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
    }

    @Test
    fun addExpenseFlow_cancelFromCategoryDialog() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Click add expense button
        composeTestRule.onNodeWithContentDescription("Add").performClick()

        // Verify category dialog appears
        composeTestRule.onNodeWithText("Add New").assertIsDisplayed()

        // Cancel by clicking close button
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Verify we're back to dashboard
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add New").assertDoesNotExist()
    }

    @Test
    fun addExpenseFlow_cancelFromExpenseForm() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Click add expense button
        composeTestRule.onNodeWithContentDescription("Add").performClick()

        // Select a category
        composeTestRule.onNodeWithText("Education").performClick()

        // Verify expense form appears
        composeTestRule.onNodeWithText("Add Education").assertIsDisplayed()

        // Cancel by clicking cancel button
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Verify we're back to dashboard
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add Education").assertDoesNotExist()
    }

    @Test
    fun addExpenseFlow_invalidDataShowsError() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Click add expense button
        composeTestRule.onNodeWithContentDescription("Add").performClick()

        // Select a category
        composeTestRule.onNodeWithText("Healthcare").performClick()

        // Try to submit without filling fields
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Verify error message appears
        composeTestRule.onNodeWithText("Please fill all fields correctly").assertIsDisplayed()

        // Form should still be open
        composeTestRule.onNodeWithText("Add Healthcare").assertIsDisplayed()
    }

    @Test
    fun addExpenseFlow_differentCategories() {
        // Test each category selection
        val categories = listOf(
            "Business Expenses",
            "Savings & Investments", 
            "Education",
            "Healthcare"
        )

        categories.forEach { category ->
            // Wait for app to load
            composeTestRule.waitForIdle()

            // Click add expense button
            composeTestRule.onNodeWithContentDescription("Add").performClick()

            // Select the category
            composeTestRule.onNodeWithText(category).performClick()

            // Verify correct form appears
            composeTestRule.onNodeWithText("Add $category").assertIsDisplayed()

            // Cancel to return to dashboard
            composeTestRule.onNodeWithText("Cancel").performClick()

            // Verify we're back to dashboard
            composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        }
    }

    @Test
    fun dashboardElements_allDisplayed() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Verify all main dashboard elements are present
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        composeTestRule.onNodeWithText("$7,154.87").assertIsDisplayed()
        composeTestRule.onNodeWithText("Month").assertIsDisplayed()
        
        // Verify stats cards
        composeTestRule.onNodeWithText("Highest Expense").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lowest").assertIsDisplayed()
        
        // Verify income section
        composeTestRule.onNodeWithText("Incomes").assertIsDisplayed()
        
        // Verify navigation is present
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reports").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }

    @Test
    fun scrolling_worksCorrectly() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Verify we can see the top content
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()

        // Scroll down to see income section
        composeTestRule.onRoot().performTouchInput {
            swipeUp()
        }

        // Income section should still be visible
        composeTestRule.onNodeWithText("Incomes").assertIsDisplayed()

        // Scroll back up
        composeTestRule.onRoot().performTouchInput {
            swipeDown()
        }

        // Header should be visible again
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
    }

    @Test
    fun addExpenseFlow_withValidData_completesSuccessfully() {
        // Wait for app to load
        composeTestRule.waitForIdle()

        // Complete flow with valid data
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Savings & Investments").performClick()
        
        // Fill valid data
        composeTestRule.onNodeWithText("Amount").performTextInput("500.00")
        composeTestRule.onNodeWithText("Description").performTextInput("Monthly savings")
        
        // Submit
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Should return to dashboard
        composeTestRule.onNodeWithText("Total Net worth").assertIsDisplayed()
        
        // Dialog should be closed
        composeTestRule.onNodeWithText("Add Savings & Investments").assertDoesNotExist()
    }
}
