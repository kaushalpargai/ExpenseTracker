package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.data.CategorySummary

@Composable
fun CategorySummaryCard(
    categorySummary: CategorySummary,
    totalExpenses: Double,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalExpenses > 0) {
        (categorySummary.totalAmount / totalExpenses * 100)
    } else {
        0.0
    }
    
    val categoryColor = getCategoryColor(categorySummary.category)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category indicator
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(categoryColor, RoundedCornerShape(3.dp))
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Category name
                Text(
                    text = categorySummary.category,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.weight(1f)
                )
                
                // Amount
                Text(
                    text = "₹${String.format("%.2f", categorySummary.totalAmount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = { (percentage / 100).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = categoryColor,
                trackColor = Color(0xFFE2E8F0),
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Percentage and count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${categorySummary.expenseCount} expenses",
                    fontSize = 12.sp,
                    color = Color(0xFF718096)
                )
                
                Text(
                    text = "${String.format("%.1f", percentage)}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = categoryColor
                )
            }
        }
    }
}
