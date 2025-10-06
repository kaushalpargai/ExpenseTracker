package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.components.AddExpenseDialog
import com.example.expensetracker.ui.components.BottomNavigationBar
import com.example.expensetracker.ui.screens.DashboardScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ExpenseDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao())

        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp(
                    viewModel = viewModel(
                        factory = ExpenseViewModel.Factory(repository)
                    )
                )
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp(viewModel: ExpenseViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        when (selectedTab) {
            0 -> DashboardScreen(
                viewModel = viewModel,
                onAddExpenseClick = { showAddDialog = true }
            )
            1 -> ReportsScreen() // Placeholder
            2 -> BudgetScreen() // Placeholder  
            3 -> AccountScreen() // Placeholder
        }
        
        // Bottom Navigation
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        
        // Add Expense Dialog
        if (showAddDialog) {
            AddExpenseDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { amount, description, category ->
                    viewModel.addExpense(amount, description, category)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun ReportsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Reports Screen - Coming Soon")
    }
}

@Composable
fun BudgetScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Budget Screen - Coming Soon")
    }
}

@Composable
fun AccountScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Account Screen - Coming Soon")
    }
}