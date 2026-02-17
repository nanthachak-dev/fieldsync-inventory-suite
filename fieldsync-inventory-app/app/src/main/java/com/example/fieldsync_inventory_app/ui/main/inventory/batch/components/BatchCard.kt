package com.example.fieldsync_inventory_app.ui.main.inventory.batch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BatchCard(
    item: BatchCardData,
    odd: Boolean = false,
    onClick: () -> Unit = {}
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    val cardColor = if (odd) Color(0xFFFDFCFB) else Color(0xFFF7F4F1)
    val accentColor = Color(0xFF8D6E63) // Refined brown for light theme
    val goldColor = Color(0xFFFBC02D)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = goldColor.copy(alpha = 0.1f)),
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Accent Strip
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(goldColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Batch #${item.batchId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Generation Tag
                    Surface(
                        color = accentColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = item.generationName,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusIndicator(
                        label = if (item.grading) "Graded" else "Ungraded", 
                        active = item.grading
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatusIndicator(
                        label = if (item.germination) "Germinated" else "Ungerminated", 
                        active = item.germination
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Current Stock",
                    fontSize = 10.sp,
                    color = accentColor.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${formatter.format(item.stock)} Kg",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF3E2723) // Very deep brown
                )
            }
        }
    }
}

@Composable
private fun StatusIndicator(label: String, active: Boolean) {
    val activeColor = Color(0xFF4CAF50) // Vibrant green
    val inactiveColor = Color(0xFFE53935) // Vibrant red
    val color = if (active) activeColor else inactiveColor

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF5D4037).copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBatchCard() {
    RCRCSeedManagerTheme {
        BatchCard(
            item = BatchCardData(
                batchId = 23998,
                varietyId = 1,
                varietyName = "RX-78-2",
                year = 2025,
                seasonId = 1,
                seasonName = "WET",
                seasonDescription = "Wet Season",
                generationId = 1,
                generationName = "R1",
                grading = true,
                germination = true,
                stock = 2000.0
            ),
            odd = true,
            onClick = {}
        )
    }
}
