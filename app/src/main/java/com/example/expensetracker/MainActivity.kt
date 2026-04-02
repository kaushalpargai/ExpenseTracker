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
import com.example.expensetracker.ai.GeminiService
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.screens.AIInsightsScreen
import com.example.expensetracker.ui.screens.CategoryAnalyticsScreen
import com.example.expensetracker.ui.screens.HomeScreen
import com.example.expensetracker.ui.components.BottomNavigationBar
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import java.io.FileInputStream
import java.util.Properties

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ExpenseDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao())
        
        // Try to load Gemini API key from local.properties
        val geminiService = try {
            val properties = Properties()
            val localPropertiesFile = java.io.File(applicationContext.filesDir.parent, "../../../local.properties")
            if (localPropertiesFile.exists()) {
                properties.load(FileInputStream(localPropertiesFile))
                val apiKey = properties.getProperty("GEMINI_API_KEY")
                if (!apiKey.isNullOrBlank()) {
                    GeminiService(apiKey)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp(
                    viewModel = viewModel(
                        factory = ExpenseViewModel.Factory(repository, geminiService)
                    )
                )
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp(viewModel: ExpenseViewModel) {
    var selectedTab by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        when (selectedTab) {
            0 -> HomeScreen(viewModel = viewModel)
            1 -> CategoryAnalyticsScreen(viewModel = viewModel)
            2 -> AIInsightsScreen(viewModel = viewModel)
            3 -> AccountScreen()
        }
        
        // Bottom Navigation
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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