package com.example.fieldsync_inventory_app.ui.main.report.stock_report.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StockReportSeedBatchData(
    val seasonName: String,
    val seedType: String,
    val quantity: Int,
    val batchNumber: String,
    val isGraded: Boolean,
    val isGerminated: Boolean,
    val date: String
)

// Color Palette
val DarkGreen = Color(0xFF0D2818)
val MediumGreen = Color(0xFF1A3D2B)
val AccentGreen = Color(0xFF00FF7F)
val StatusGreen = Color(0xFF4CAF50)
val StatusBlue = Color(0xFF2196F3)
val StatusOrange = Color(0xFFFF9800)
val StatusGray = Color(0xFF9E9E9E)

@Composable
fun StockReportSeedBatchCard(batch: StockReportSeedBatchData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle card click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MediumGreen
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Seed Icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(DarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AccentGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = batch.seasonName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = batch.seedType,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${batch.quantity} kg",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = batch.batchNumber,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusChip(
                        text = if (batch.isGraded) "GRADED" else "UNGRADED",
                        icon = if (batch.isGraded) Icons.Default.CheckCircle else Icons.Default.Delete,
                        color = if (batch.isGraded) StatusGreen else StatusGray
                    )

                    StatusChip(
                        text = if (batch.isGerminated) "GERMINATED" else "UNGERMINATED",
                        icon = if (batch.isGerminated) Icons.Default.ThumbUp else Icons.Default.Build,
                        color = if (batch.isGerminated) StatusBlue else StatusOrange
                    )
                }

                Text(
                    text = batch.date,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStockReportSeedBatchCard(){
    val batch = StockReportSeedBatchData(
        seasonName = "2023-2024",
        seedType = "Rice",
        quantity = 100,
        batchNumber = "12345",
        isGraded = true,
        isGerminated = true,
        date = "12/01/2023"
    )

    StockReportSeedBatchCard(batch)
}

@Composable
fun StatusChip(
    text: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableStateOf(1) }

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = DarkGreen,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Dashboard"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Inventory"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 },
            icon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Lab"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = { selectedIndex = 3 },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                indicatorColor = Color.Transparent
            )
        )
    }
}