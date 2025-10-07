package com.example.expensetracker.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddCategoryDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addCategoryDialog_displaysCorrectly() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = {}
                )
            }
        }

        // Verify dialog title
        composeTestRule.onNodeWithText("Add New").assertIsDisplayed()
        
        // Verify close button
        composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
        
        // Verify all categories are displayed
        composeTestRule.onNodeWithText("Business Expenses").assertIsDisplayed()
        composeTestRule.onNodeWithText("Savings & Investments").assertIsDisplayed()
        composeTestRule.onNodeWithText("Education").assertIsDisplayed()
        composeTestRule.onNodeWithText("Healthcare").assertIsDisplayed()
    }

    @Test
    fun addCategoryDialog_categoryDescriptionsDisplayed() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = {}
                )
            }
        }

        // Verify category descriptions are displayed
        composeTestRule.onNodeWithText("Track business marketing software supplies travel office costs and expenses.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Track health insurance medical expenses and healthcare related costs.")
            .assertIsDisplayed()
    }

    @Test
    fun addCategoryDialog_closeButtonTriggersCallback() {
        var dismissCalled = false

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = { dismissCalled = true },
                    onCategorySelected = {}
                )
            }
        }

        // Click close button
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Verify callback was triggered
        assert(dismissCalled)
    }

    @Test
    fun addCategoryDialog_categorySelectionTriggersCallback() {
        var selectedCategory: ExpenseCategory? = null

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = { category -> selectedCategory = category }
                )
            }
        }

        // Click on Business Expenses category
        composeTestRule.onNodeWithText("Business Expenses").performClick()

        // Verify callback was triggered with correct category
        assert(selectedCategory != null)
        assert(selectedCategory?.name == "Business Expenses")
    }

    @Test
    fun addCategoryDialog_educationCategorySelectionWorks() {
        var selectedCategory: ExpenseCategory? = null

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = { category -> selectedCategory = category }
                )
            }
        }

        // Click on Education category
        composeTestRule.onNodeWithText("Education").performClick()

        // Verify callback was triggered with correct category
        assert(selectedCategory != null)
        assert(selectedCategory?.name == "Education")
        assert(selectedCategory?.emoji == "🎓")
    }

    @Test
    fun addCategoryDialog_savingsAndInvestmentsSelectionWorks() {
        var selectedCategory: ExpenseCategory? = null

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = { category -> selectedCategory = category }
                )
            }
        }

        // Click on Savings & Investments category
        composeTestRule.onNodeWithText("Savings & Investments").performClick()

        // Verify callback was triggered with correct category
        assert(selectedCategory != null)
        assert(selectedCategory?.name == "Savings & Investments")
        assert(selectedCategory?.emoji == "💰")
    }

    @Test
    fun addCategoryDialog_healthcareSelectionWorks() {
        var selectedCategory: ExpenseCategory? = null

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = { category -> selectedCategory = category }
                )
            }
        }

        // Click on Healthcare category
        composeTestRule.onNodeWithText("Healthcare").performClick()

        // Verify callback was triggered with correct category
        assert(selectedCategory != null)
        assert(selectedCategory?.name == "Healthcare")
    }

    @Test
    fun addCategoryDialog_categoryIconsDisplayed() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = {}
                )
            }
        }

        // Verify category emojis are displayed (they should be in the UI)
        // Note: Emojis might be rendered as text, so we check for their presence
        composeTestRule.onNodeWithText("💼").assertIsDisplayed()
        composeTestRule.onNodeWithText("💰").assertIsDisplayed()
        composeTestRule.onNodeWithText("🎓").assertIsDisplayed()
    }

    @Test
    fun addCategoryDialog_scrollableContent() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddCategoryDialog(
                    onDismiss = {},
                    onCategorySelected = {}
                )
            }
        }

        // Verify all categories are accessible (should be scrollable if needed)
        composeTestRule.onNodeWithText("Business Expenses").assertIsDisplayed()
        composeTestRule.onNodeWithText("Healthcare").assertIsDisplayed()
        
        // All categories should be visible without scrolling in this case
        // but the LazyColumn should handle scrolling if more categories are added
    }
}
