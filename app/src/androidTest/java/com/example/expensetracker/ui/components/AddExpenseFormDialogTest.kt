package com.example.expensetracker.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddExpenseFormDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCategory = ExpenseCategory(
        name = "Business Expenses",
        description = "Track business marketing software supplies travel office costs and expenses.",
        emoji = "💼",
        color = Color(0xFFFF9500)
    )

    @Test
    fun addExpenseFormDialog_displaysCorrectly() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Verify dialog title with category name
        composeTestRule.onNodeWithText("Add Business Expenses").assertIsDisplayed()
        
        // Verify close button
        composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()
        
        // Verify selected category display
        composeTestRule.onNodeWithText("Business Expenses").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selected category").assertIsDisplayed()
        
        // Verify form fields
        composeTestRule.onNodeWithText("Amount").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        
        // Verify buttons
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add Expense").assertIsDisplayed()
    }

    @Test
    fun addExpenseFormDialog_categoryEmojiDisplayed() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Verify category emoji is displayed
        composeTestRule.onNodeWithText("💼").assertIsDisplayed()
    }

    @Test
    fun addExpenseFormDialog_closeButtonTriggersCallback() {
        var dismissCalled = false

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = { dismissCalled = true },
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Click close button
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Verify callback was triggered
        assert(dismissCalled)
    }

    @Test
    fun addExpenseFormDialog_cancelButtonTriggersCallback() {
        var dismissCalled = false

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = { dismissCalled = true },
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Click cancel button
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Verify callback was triggered
        assert(dismissCalled)
    }

    @Test
    fun addExpenseFormDialog_formInputWorks() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Enter amount
        composeTestRule.onNodeWithText("Amount")
            .performTextInput("100.50")
        
        // Enter description
        composeTestRule.onNodeWithText("Description")
            .performTextInput("Test expense description")

        // Verify text was entered (check placeholder text is replaced)
        composeTestRule.onNodeWithText("Enter amount").assertDoesNotExist()
        composeTestRule.onNodeWithText("Enter description").assertDoesNotExist()
    }

    @Test
    fun addExpenseFormDialog_validFormSubmission() {
        var confirmedAmount: Double? = null
        var confirmedDescription: String? = null
        var confirmedCategory: String? = null

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { amount, description, category ->
                        confirmedAmount = amount
                        confirmedDescription = description
                        confirmedCategory = category
                    }
                )
            }
        }

        // Fill in valid form data
        composeTestRule.onNodeWithText("Amount")
            .performTextInput("150.75")
        
        composeTestRule.onNodeWithText("Description")
            .performTextInput("Business lunch meeting")

        // Submit form
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Verify callback was triggered with correct data
        assert(confirmedAmount == 150.75)
        assert(confirmedDescription == "Business lunch meeting")
        assert(confirmedCategory == "Business Expenses")
    }

    @Test
    fun addExpenseFormDialog_invalidAmountShowsError() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Enter invalid amount
        composeTestRule.onNodeWithText("Amount")
            .performTextInput("invalid")
        
        composeTestRule.onNodeWithText("Description")
            .performTextInput("Valid description")

        // Try to submit
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Verify error message is shown
        composeTestRule.onNodeWithText("Please fill all fields correctly").assertIsDisplayed()
    }

    @Test
    fun addExpenseFormDialog_emptyFieldsShowError() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Try to submit without filling fields
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Verify error message is shown
        composeTestRule.onNodeWithText("Please fill all fields correctly").assertIsDisplayed()
    }

    @Test
    fun addExpenseFormDialog_zeroAmountShowsError() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Enter zero amount
        composeTestRule.onNodeWithText("Amount")
            .performTextInput("0")
        
        composeTestRule.onNodeWithText("Description")
            .performTextInput("Valid description")

        // Try to submit
        composeTestRule.onNodeWithText("Add Expense").performClick()

        // Verify error message is shown
        composeTestRule.onNodeWithText("Please fill all fields correctly").assertIsDisplayed()
    }

    @Test
    fun addExpenseFormDialog_errorDisappearsOnInput() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = testCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Try to submit empty form to show error
        composeTestRule.onNodeWithText("Add Expense").performClick()
        composeTestRule.onNodeWithText("Please fill all fields correctly").assertIsDisplayed()

        // Start typing in amount field
        composeTestRule.onNodeWithText("Amount")
            .performTextInput("100")

        // Error should disappear
        composeTestRule.onNodeWithText("Please fill all fields correctly").assertDoesNotExist()
    }

    @Test
    fun addExpenseFormDialog_differentCategoryDisplaysCorrectly() {
        val educationCategory = ExpenseCategory(
            name = "Education",
            description = "Educational expenses",
            emoji = "🎓",
            color = Color(0xFF5856D6)
        )

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                AddExpenseFormDialog(
                    category = educationCategory,
                    onDismiss = {},
                    onConfirm = { _, _, _ -> }
                )
            }
        }

        // Verify correct category is displayed
        composeTestRule.onNodeWithText("Add Education").assertIsDisplayed()
        composeTestRule.onNodeWithText("Education").assertIsDisplayed()
        composeTestRule.onNodeWithText("🎓").assertIsDisplayed()
    }
}
