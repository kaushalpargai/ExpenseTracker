package com.example.expensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.ui.theme.*

@Composable
fun ExpenseChart(
    modifier: Modifier = Modifier
) {
    // Mock data points for the chart
    val dataPoints = listOf(
        Pair("Jan 11", 2000f),
        Pair("Jan 16", 1500f),
        Pair("Jan 21", 1800f),
        Pair("Jan 26", 2200f),
        Pair("Feb 01", 2600f),
        Pair("Feb 05", 2800f)
    )
    
    Column(modifier = modifier) {
        // Chart area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawExpenseChart(dataPoints, size.width, size.height)
            }
            
            // Y-axis labels
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "3K",
                    fontSize = 10.sp,
                    color = TextTertiary
                )
                Text(
                    text = "2K",
                    fontSize = 10.sp,
                    color = TextTertiary
                )
                Text(
                    text = "0",
                    fontSize = 10.sp,
                    color = TextTertiary
                )
            }
        }
        
        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dataPoints.forEach { (label, _) ->
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = TextTertiary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun DrawScope.drawExpenseChart(
    dataPoints: List<Pair<String, Float>>,
    width: Float,
    height: Float
) {
    if (dataPoints.isEmpty()) return
    
    val maxValue = dataPoints.maxOf { it.second }
    val minValue = dataPoints.minOf { it.second }
    val valueRange = maxValue - minValue
    
    val chartWidth = width - 80.dp.toPx() // Leave space for labels
    val chartHeight = height - 40.dp.toPx()
    val startX = 40.dp.toPx()
    val startY = 20.dp.toPx()
    
    // Draw grid lines
    for (i in 0..2) {
        val y = startY + (chartHeight / 2) * i
        drawLine(
            color = ChartLine,
            start = Offset(startX, y),
            end = Offset(startX + chartWidth, y),
            strokeWidth = 1.dp.toPx()
        )
    }
    
    // Calculate points for the line
    val points = dataPoints.mapIndexed { index, (_, value) ->
        val x = startX + (chartWidth / (dataPoints.size - 1)) * index
        val normalizedValue = (value - minValue) / valueRange
        val y = startY + chartHeight - (normalizedValue * chartHeight)
        Offset(x, y)
    }
    
    // Draw the gradient area under the curve
    val path = Path().apply {
        moveTo(points.first().x, startY + chartHeight)
        points.forEach { point ->
            lineTo(point.x, point.y)
        }
        lineTo(points.last().x, startY + chartHeight)
        close()
    }
    
    // Draw gradient fill (simplified as solid color for now)
    drawPath(
        path = path,
        color = ChartPink.copy(alpha = 0.1f)
    )
    
    // Draw the main line
    for (i in 0 until points.size - 1) {
        drawLine(
            color = ChartPink,
            start = points[i],
            end = points[i + 1],
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
    
    // Draw points
    points.forEach { point ->
        drawCircle(
            color = ChartPink,
            radius = 4.dp.toPx(),
            center = point
        )
        drawCircle(
            color = Color.White,
            radius = 2.dp.toPx(),
            center = point
        )
    }
}
