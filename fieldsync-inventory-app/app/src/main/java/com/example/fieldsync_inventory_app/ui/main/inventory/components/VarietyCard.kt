package com.example.fieldsync_inventory_app.ui.main.inventory.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.R
import com.example.fieldsync_inventory_app.ui.main.inventory.model.VarietyCardData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * A composable card to display seed stock information, styled to match the provided image.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VarietyCard(
    item: VarietyCardData,
    odd: Boolean = false,
    onClick: () -> Unit = {}
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    val cardColor = if (odd) Color(0xFF4A493E) else Color(0xFF424137)
    val accentColor = Color(0xFFFBC02D) // Gold accent

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = accentColor.copy(alpha = 0.1f)),
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Seed image with premium border
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.seed_image_sample_1),
                    contentDescription = "Seed Image",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 2
                ) {
                    Text(
                        text = item.varietyName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    // Stock Badge
                    Surface(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "${formatter.format(item.stock)} Kg",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }

                FlowRow(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GenerationBadge(label = "R1", value = item.r1Stock)
                    GenerationBadge(label = "R2", value = item.r2Stock)
                    GenerationBadge(label = "R3", value = item.r3Stock)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Grid - Also handles wrapping internally
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        StatLabelValue(
                            label = "Graded", 
                            value = "${formatter.format(item.graded)} Kg",
                            r1 = item.r1Graded,
                            r2 = item.r2Graded,
                            r3 = item.r3Graded
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        StatLabelValue(
                            label = "Ungraded", 
                            value = "${formatter.format(item.ungraded)} Kg",
                            r1 = item.r1Ungraded,
                            r2 = item.r2Ungraded,
                            r3 = item.r3Ungraded
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        StatLabelValue(
                            label = "Germinated", 
                            value = "${formatter.format(item.germinated)} Kg",
                            r1 = item.r1Germinated,
                            r2 = item.r2Germinated,
                            r3 = item.r3Germinated
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        StatLabelValue(
                            label = "Ungerminated", 
                            value = "${formatter.format(item.ungerminated)} Kg",
                            r1 = item.r1Ungerminated,
                            r2 = item.r2Ungerminated,
                            r3 = item.r3Ungerminated
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GenerationBadge(label: String, value: Double) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label: ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.5f)
        )
        Text(
            text = "${formatter.format(value)} Kg",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatLabelValue(
    label: String, 
    value: String,
    r1: Double = 0.0,
    r2: Double = 0.0,
    r3: Double = 0.0
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.US) }
    Column {
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFBC02D).copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$label: ",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp) // Indent if wrapped
            )
        }
        
        // Use FlowRow for R-breakdown as well
        FlowRow(
            modifier = Modifier.padding(start = 10.dp, top = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val rStyle = Color.White.copy(alpha = 0.4f)
            Text(text = "R1: ${formatter.format(r1)}", fontSize = 9.sp, color = rStyle)
            Text(text = "|", fontSize = 9.sp, color = rStyle.copy(alpha = 0.2f))
            Text(text = "R2: ${formatter.format(r2)}", fontSize = 9.sp, color = rStyle)
            Text(text = "|", fontSize = 9.sp, color = rStyle.copy(alpha = 0.2f))
            Text(text = "R3: ${formatter.format(r3)}", fontSize = 9.sp, color = rStyle)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStockInfoCard() {
    RCRCSeedManagerTheme {
        VarietyCard(
            item = VarietyCardData(
                varietyId = 1,
                varietyName = "TDK1",
                stock = 12000.0,
                graded = 3250.0,
                ungraded = 9000.0,
                germinated = 5250.0,
                ungerminated = 6750.0
            )
        )
    }
}