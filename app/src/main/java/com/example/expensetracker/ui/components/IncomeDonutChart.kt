package com.example.expensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.ui.theme.*

@Composable
fun IncomeDonutChart(
    totalIncome: Double,
    modifier: Modifier = Modifier
) {
    // Mock data for income categories
    val incomeData = listOf(
        IncomeCategory("Monthly salary", 75.5f, ChartPurple),
        IncomeCategory("Business Project", 14.5f, ChartPink),
        IncomeCategory("Other", 10.0f, TextTertiary)
    )
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Donut Chart
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawDonutChart(incomeData, size.width, size.height)
            }
            
            // Center text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$${String.format("%.2f", totalIncome)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
        
        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            incomeData.forEach { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(category.color)
                    )
                    
                    Column {
                        Text(
                            text = category.name,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${category.percentage}%",
                            fontSize = 14.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawDonutChart(
    data: List<IncomeCategory>,
    width: Float,
    height: Float
) {
    val center = Offset(width / 2, height / 2)
    val radius = minOf(width, height) / 2 - 10.dp.toPx()
    val strokeWidth = 12.dp.toPx()
    
    var startAngle = -90f // Start from top
    
    data.forEach { category ->
        val sweepAngle = (category.percentage / 100f) * 360f
        
        drawArc(
            color = category.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(
                center.x - radius,
                center.y - radius
            ),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
        
        startAngle += sweepAngle
    }
}

data class IncomeCategory(
    val name: String,
    val percentage: Float,
    val color: Color
)
