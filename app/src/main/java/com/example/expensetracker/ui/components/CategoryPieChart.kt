package com.example.expensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.data.CategorySummary

@Composable
fun CategoryPieChart(
    categorySummaries: List<CategorySummary>,
    totalAmount: Double,
    modifier: Modifier = Modifier
) {
    if (categorySummaries.isEmpty() || totalAmount <= 0) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No expenses yet",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pie Chart
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        ) {
            val canvasSize = size.minDimension
            val radius = canvasSize / 2
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            var startAngle = -90f
            
            categorySummaries.forEach { category ->
                val sweepAngle = ((category.totalAmount / totalAmount) * 360).toFloat()
                val color = when (category.category.lowercase()) {
                    "food" -> Color(0xFFFF6B9D)
                    "transport" -> Color(0xFF4299E1)
                    "shopping" -> Color(0xFFED8936)
                    "entertainment" -> Color(0xFF9F7AEA)
                    "bills" -> Color(0xFFECC94B)
                    "healthcare" -> Color(0xFF48BB78)
                    "education" -> Color(0xFF38B2AC)
                    else -> Color(0xFF8B5CF6)
                }
                
                // Draw pie slice
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2)
                )
                
                startAngle += sweepAngle
            }
            
            // Draw white circle in center for donut effect
            drawCircle(
                color = Color.White,
                radius = radius * 0.5f,
                center = Offset(centerX, centerY)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categorySummaries.take(5).forEach { category ->
                val percentage = (category.totalAmount / totalAmount * 100)
                CategoryLegendItem(
                    category = category.category,
                    amount = category.totalAmount,
                    percentage = percentage,
                    color = getCategoryColor(category.category)
                )
            }
        }
    }
}

@Composable
private fun CategoryLegendItem(
    category: String,
    amount: Double,
    percentage: Double,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, RoundedCornerShape(3.dp))
            )
            
            Text(
                text = category,
                fontSize = 14.sp,
                color = Color(0xFF2D3748)
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "₹${String.format("%.2f", amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748)
            )
            
            Text(
                text = "${String.format("%.1f", percentage)}%",
                fontSize = 12.sp,
                color = Color(0xFF718096)
            )
        }
    }
}
