package com.example.expensetracker.data

import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ExpenseTest {

    @Test
    fun expense_creation_setsPropertiesCorrectly() {
        val date = Date()
        val expense = Expense(
            id = 1,
            amount = 100.50,
            description = "Test Expense",
            category = "Food",
            date = date
        )

        assertEquals(1, expense.id)
        assertEquals(100.50, expense.amount)
        assertEquals("Test Expense", expense.description)
        assertEquals("Food", expense.category)
        assertEquals(date, expense.date)
    }

    @Test
    fun expense_defaultId_isZero() {
        val expense = Expense(
            amount = 50.0,
            description = "Default ID Test",
            category = "Transport",
            date = Date()
        )

        assertEquals(0, expense.id)
    }

    @Test
    fun expense_equality_worksCorrectly() {
        val date = Date()
        val expense1 = Expense(1, 100.0, "Test", "Food", date)
        val expense2 = Expense(1, 100.0, "Test", "Food", date)
        val expense3 = Expense(2, 100.0, "Test", "Food", date)

        assertEquals(expense1, expense2)
        assertNotEquals(expense1, expense3)
    }

    @Test
    fun expense_copy_worksCorrectly() {
        val originalExpense = Expense(
            id = 1,
            amount = 75.0,
            description = "Original",
            category = "Food",
            date = Date()
        )

        val copiedExpense = originalExpense.copy(
            description = "Modified",
            amount = 80.0
        )

        assertEquals(1, copiedExpense.id)
        assertEquals(80.0, copiedExpense.amount)
        assertEquals("Modified", copiedExpense.description)
        assertEquals("Food", copiedExpense.category)
        assertEquals(originalExpense.date, copiedExpense.date)
    }

    @Test
    fun expense_toString_containsAllProperties() {
        val expense = Expense(
            id = 5,
            amount = 123.45,
            description = "Test Description",
            category = "Test Category",
            date = Date()
        )

        val stringRepresentation = expense.toString()

        assert(stringRepresentation.contains("5"))
        assert(stringRepresentation.contains("123.45"))
        assert(stringRepresentation.contains("Test Description"))
        assert(stringRepresentation.contains("Test Category"))
    }

    @Test
    fun expense_withNegativeAmount_allowsNegativeValues() {
        val expense = Expense(
            amount = -50.0,
            description = "Refund",
            category = "Income",
            date = Date()
        )

        assertEquals(-50.0, expense.amount)
    }

    @Test
    fun expense_withEmptyStrings_allowsEmptyValues() {
        val expense = Expense(
            amount = 100.0,
            description = "",
            category = "",
            date = Date()
        )

        assertEquals("", expense.description)
        assertEquals("", expense.category)
    }

    @Test
    fun expense_withSpecialCharacters_handlesCorrectly() {
        val expense = Expense(
            amount = 99.99,
            description = "Café & Restaurant - 50% off! @#$%",
            category = "Food & Drinks",
            date = Date()
        )

        assertEquals("Café & Restaurant - 50% off! @#$%", expense.description)
        assertEquals("Food & Drinks", expense.category)
    }

    @Test
    fun expense_hashCode_consistentWithEquals() {
        val date = Date()
        val expense1 = Expense(1, 100.0, "Test", "Food", date)
        val expense2 = Expense(1, 100.0, "Test", "Food", date)

        assertEquals(expense1.hashCode(), expense2.hashCode())
    }
}
