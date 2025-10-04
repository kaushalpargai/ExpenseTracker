package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.components.AddExpenseDialog
import com.example.expensetracker.ui.components.ExpenseList
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import java.text.NumberFormat

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerApp(viewModel: ExpenseViewModel) {
    val expenses by viewModel.expenses.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add expense")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Total expenses card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Total Expenses",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = NumberFormat.getCurrencyInstance().format(totalExpenses ?: 0.0),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            // Expense list
            ExpenseList(
                expenses = expenses,
                onDeleteExpense = { viewModel.deleteExpense(it) }
            )
        }

        if (showAddDialog) {
            AddExpenseDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { amount, description, category ->
                    viewModel.addExpense(amount, description, category)
                }
            )
        }
    }
}