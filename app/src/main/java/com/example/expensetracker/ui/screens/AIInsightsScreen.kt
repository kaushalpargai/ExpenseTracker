package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.components.AIInsightCard
import com.example.expensetracker.ui.components.RecommendationsList
import com.example.expensetracker.ui.theme.*

@Composable
fun AIInsightsScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val aiAnalysis by viewModel.aiAnalysis.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisError by viewModel.analysisError.collectAsState()
    val monthlyExpenses by viewModel.monthlyExpenses.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AI Insights",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "Powered by Gemini AI",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { viewModel.requestAIAnalysis() },
                    enabled = !isAnalyzing && monthlyExpenses.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isAnalyzing) "Analyzing..." else "Analyze")
                }
            }
        }

        // Error Message
        if (analysisError != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEF2F2)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "⚠️", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "Error",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626)
                            )
                            Text(
                                text = analysisError!!,
                                fontSize = 14.sp,
                                color = Color(0xFF991B1B)
                            )
                        }
                    }
                }
            }
        }

        // Loading State
        if (isAnalyzing) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF8B5CF6)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Analyzing your expenses...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2D3748)
                        )
                    }
                }
            }
        }

        // AI Analysis Results
        if (aiAnalysis != null) {
            val analysis = aiAnalysis!!
            
            // Weekly Comparison
            item {
                AIInsightCard(
                    title = "Weekly Comparison",
                    content = analysis.weeklyComparison,
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF10B981)
                )
            }

            // Monthly Comparison
            item {
                AIInsightCard(
                    title = "Monthly Comparison",
                    content = analysis.monthlyComparison,
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF3B82F6)
                )
            }

            // Recommendations
            item {
                RecommendationsList(
                    recommendations = analysis.recommendations
                )
            }

            // Insights
            item {
                AIInsightCard(
                    title = "Key Insights",
                    content = analysis.insights,
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFFF59E0B)
                )
            }
        }

        // Empty State
        if (aiAnalysis == null && !isAnalyzing && analysisError == null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🤖",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Get AI Insights",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3748)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (monthlyExpenses.isEmpty()) {
                                "Add some expenses first, then tap 'Analyze' to get personalized insights"
                            } else {
                                "Tap 'Analyze' above to get AI-powered insights about your spending"
                            },
                            fontSize = 14.sp,
                            color = Color(0xFF718096),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
