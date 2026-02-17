package com.example.fieldsync_inventory_app.ui.main.report.stock_report.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.StockReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.displayGermination
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SimpleStockReportSeedBatchCard(
    item: StockReportSeedBatch,
    odd: Boolean = false,
    onClick: () -> Unit = {} // Add an onClick lambda parameter for click effect
){
    val cardColor = if (odd) Color(0xFFBCDAF1).copy(alpha = 0.9f) else Color(0xFFBCDAF1).copy(alpha = 0.8f)
    val interactionSource = remember { MutableInteractionSource() } // Click effect
    val formatter = remember {
        NumberFormat.getNumberInstance(Locale.US)
    }
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable( // Add click effect
                interactionSource = interactionSource,
                indication = ripple(color = Color.White), // Customize ripple color
                onClick = onClick
            ),
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Column to hold the title and description texts
            Column {
                // The title text
                Text(
                    text = item.generationName, // Variety Name
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )

                Spacer(modifier = Modifier.height(4.dp))
                // Second line of details
                Text(
                    // Generation | Grading | Germination
                    text = "Graded: ${formatter.format(item.graded)} Kg | Ungraded: ${formatter.format(item.ungraded)} Kg  |   ${item.displayGermination()}",
                    fontSize = 12.sp,
                    color = Color.Black,
                    style = TextStyle(lineHeight = 14.sp) // Adjust this value as needed
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSimpleStockReportSeedBatchCard() {
    RCRCSeedManagerTheme {
        SimpleStockReportSeedBatchCard(
            item = StockReportSeedBatch(
                varietyId = 1,
                varietyName = "RX-78-2",
                year = 2025,
                seasonId = 1,
                seasonName = "WET",
                seasonDescription = "Wet Season",
                generationId = 1,
                generationName = "R1",
                graded = 3300.0,
                ungraded = 2300.0,
                germination = true
            ),
            odd = true,
            onClick = {}
        )
    }
}
