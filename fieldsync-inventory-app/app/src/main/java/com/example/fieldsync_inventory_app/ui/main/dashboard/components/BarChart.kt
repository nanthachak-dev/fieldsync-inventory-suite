package com.example.fieldsync_inventory_app.ui.main.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.ui.main.dashboard.model.BarChartData
import java.util.Locale

@Composable
fun BarChart(data: List<BarChartData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6C635B), RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        data.forEach { item ->
            // Calculate the value in tons and format it
            //val valueInTons = item.value / 1000f
            val formattedValue = String.format(Locale.US, "%.2f Kg", item.value)
            
            // Calculate bar width percentage
            val barPercentage = if (maxValue > 0) item.value / maxValue else 0f
            
            val barColor = when {
                item.value <= 0.3f * maxValue -> Color(0xFFBE4743)
                item.value <= 0.6f * maxValue -> Color(0xFFFEF10F)
                else -> Color(0xFF8CBE43)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                // Combined Variety Name and Value Text: e.g., "RX-78-2 [1.2t]"
                Text(
                    text = "${item.varietyName} [$formattedValue]",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Horizontal Bar
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(barPercentage.coerceAtLeast(0.01f))
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(barColor)
                )
            }
        }
    }
}
