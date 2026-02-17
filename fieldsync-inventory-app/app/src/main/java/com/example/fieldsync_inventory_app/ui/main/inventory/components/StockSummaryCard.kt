package com.example.fieldsync_inventory_app.ui.main.inventory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StockSummaryCard(
    summary: StockSummary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE6B800), // Rich Golden
                            Color(0xFFFFD700)  // Bright Gold
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Stock Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Total Stock Highlight
                Column {
                    Text(
                        text = "Total Stock",
                        fontSize = 14.sp,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${formatter.format(summary.totalStock)} Kg",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // R1, R2, R3 Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StockMetricItem("R1", summary.totalR1Stock, formatter)
                    StockMetricItem("R2", summary.totalR2Stock, formatter)
                    StockMetricItem("R3", summary.totalR3Stock, formatter)
                }

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Black.copy(alpha = 0.1f)
                )

                // Detailed Status Sections
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    StatusBreakdownItem(
                        title = "Graded",
                        total = summary.totalGraded,
                        r1 = summary.totalR1Graded,
                        r2 = summary.totalR2Graded,
                        r3 = summary.totalR3Graded,
                        formatter = formatter,
                        modifier = Modifier.weight(1f)
                    )
                    StatusBreakdownItem(
                        title = "Ungraded",
                        total = summary.totalUngraded,
                        r1 = summary.totalR1Ungraded,
                        r2 = summary.totalR2Ungraded,
                        r3 = summary.totalR3Ungraded,
                        formatter = formatter,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    StatusBreakdownItem(
                        title = "Germinated",
                        total = summary.totalGerminated,
                        r1 = summary.totalR1Germinated,
                        r2 = summary.totalR2Germinated,
                        r3 = summary.totalR3Germinated,
                        formatter = formatter,
                        modifier = Modifier.weight(1f)
                    )
                    StatusBreakdownItem(
                        title = "Ungerminated",
                        total = summary.totalUngerminated,
                        r1 = summary.totalR1Ungerminated,
                        r2 = summary.totalR2Ungerminated,
                        r3 = summary.totalR3Ungerminated,
                        formatter = formatter,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBreakdownItem(
    title: String,
    total: Double,
    r1: Double,
    r2: Double,
    r3: Double,
    formatter: NumberFormat,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${formatter.format(total)} Kg",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Compact R1, R2, R3 list
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            SmallMetricRow("R1", r1, formatter)
            SmallMetricRow("R2", r2, formatter)
            SmallMetricRow("R3", r3, formatter)
        }
    }
}

@Composable
private fun SmallMetricRow(label: String, value: Double, formatter: NumberFormat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Black.copy(alpha = 0.6f)
        )
        Text(
            text = "${formatter.format(value)} Kg",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun StockMetricItem(
    label: String,
    value: Double,
    formatter: NumberFormat
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Black.copy(alpha = 0.7f)
        )
        Text(
            text = "${formatter.format(value)} Kg",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun StockSummaryCardPreview() {
    RCRCSeedManagerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            StockSummaryCard(
                summary = StockSummary(
                    totalStock = 45000.0,
                    totalR1Stock = 15000.0,
                    totalR2Stock = 15000.0,
                    totalR3Stock = 15000.0,
                    totalGraded = 30000.0,
                    totalUngraded = 15000.0,
                    totalGerminated = 35000.0,
                    totalUngerminated = 10000.0,
                    totalR1Graded = 10000.0,
                    totalR2Graded = 10000.0,
                    totalR3Graded = 10000.0,
                    totalR1Ungraded = 5000.0,
                    totalR2Ungraded = 5000.0,
                    totalR3Ungraded = 5000.0,
                    totalR1Germinated = 12000.0,
                    totalR2Germinated = 12000.0,
                    totalR3Germinated = 11000.0,
                    totalR1Ungerminated = 3000.0,
                    totalR2Ungerminated = 3000.0,
                    totalR3Ungerminated = 4000.0
                )
            )
        }
    }
}
