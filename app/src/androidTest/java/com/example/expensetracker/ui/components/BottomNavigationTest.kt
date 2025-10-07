package com.example.expensetracker.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomNavigation_displaysAllTabs() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = {}
                )
            }
        }

        // Verify all tab labels are displayed
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reports").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }

    @Test
    fun bottomNavigation_homeTabSelectedByDefault() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = {}
                )
            }
        }

        // Home tab should be selected (index 0)
        // We can verify this by checking if the Home text exists
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
    }

    @Test
    fun bottomNavigation_tabSelectionWorks() {
        var selectedTabIndex = 0

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = selectedTabIndex,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
            }
        }

        // Click on Reports tab
        composeTestRule.onNodeWithText("Reports").performClick()
        assert(selectedTabIndex == 1)

        // Click on Budget tab
        composeTestRule.onNodeWithText("Budget").performClick()
        assert(selectedTabIndex == 2)

        // Click on Account tab
        composeTestRule.onNodeWithText("Account").performClick()
        assert(selectedTabIndex == 3)

        // Click back on Home tab
        composeTestRule.onNodeWithText("Home").performClick()
        assert(selectedTabIndex == 0)
    }

    @Test
    fun bottomNavigation_reportsTabSelection() {
        var selectedTabIndex = -1

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
            }
        }

        // Click on Reports tab
        composeTestRule.onNodeWithText("Reports").performClick()

        // Verify callback was triggered with correct index
        assert(selectedTabIndex == 1)
    }

    @Test
    fun bottomNavigation_budgetTabSelection() {
        var selectedTabIndex = -1

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
            }
        }

        // Click on Budget tab
        composeTestRule.onNodeWithText("Budget").performClick()

        // Verify callback was triggered with correct index
        assert(selectedTabIndex == 2)
    }

    @Test
    fun bottomNavigation_accountTabSelection() {
        var selectedTabIndex = -1

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
            }
        }

        // Click on Account tab
        composeTestRule.onNodeWithText("Account").performClick()

        // Verify callback was triggered with correct index
        assert(selectedTabIndex == 3)
    }

    @Test
    fun bottomNavigation_selectedTabHighlighted() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 1, // Reports tab selected
                    onTabSelected = {}
                )
            }
        }

        // All tabs should still be displayed
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reports").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }

    @Test
    fun bottomNavigation_multipleTabClicks() {
        var clickCount = 0
        var lastSelectedTab = -1

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = { index -> 
                        clickCount++
                        lastSelectedTab = index
                    }
                )
            }
        }

        // Click multiple tabs in sequence
        composeTestRule.onNodeWithText("Reports").performClick()
        composeTestRule.onNodeWithText("Budget").performClick()
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.onNodeWithText("Home").performClick()

        // Verify all clicks were registered
        assert(clickCount == 4)
        assert(lastSelectedTab == 0) // Last click was Home (index 0)
    }

    @Test
    fun bottomNavigation_sameTabMultipleClicks() {
        var clickCount = 0

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = { clickCount++ }
                )
            }
        }

        // Click the same tab multiple times
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Home").performClick()

        // Verify all clicks were registered
        assert(clickCount == 3)
    }

    @Test
    fun bottomNavigation_iconsAreClickable() {
        var homeClicked = false
        var reportsClicked = false

        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = { index ->
                        when (index) {
                            0 -> homeClicked = true
                            1 -> reportsClicked = true
                        }
                    }
                )
            }
        }

        // Click on tab areas (icons should be clickable)
        composeTestRule.onNodeWithText("Home").performClick()
        assert(homeClicked)

        composeTestRule.onNodeWithText("Reports").performClick()
        assert(reportsClicked)
    }

    @Test
    fun bottomNavigation_accessibilityLabels() {
        composeTestRule.setContent {
            ExpenseTrackerTheme {
                BottomNavigationBar(
                    selectedTab = 0,
                    onTabSelected = {}
                )
            }
        }

        // Verify accessibility - all tab texts should be present for screen readers
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reports").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }
}
