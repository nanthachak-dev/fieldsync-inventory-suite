package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.R
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.model.SeedBatchStockInData
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.SeedBatchResult
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import kotlin.math.round

@Composable
fun SeedBatchCard(
    seedBatchData: SeedBatchResult? = null, // Data from SelectSeedBatchScreen
    onClick: () -> Unit = {}, // Add an onClick lambda parameter for click effect
    highlightTask: String? = null
) {
    // Helper function moved inside or defined externally if preferred
    fun getDetailText(seedBatch: SeedBatchResult): AnnotatedString {
        return buildAnnotatedString {
            // Define all parts of the string
            val details = listOf(
                "Year" to seedBatch.year,
                "Season" to seedBatch.season,
                "Grading" to seedBatch.grading,
                "Generation" to seedBatch.generation,
                "Germination" to seedBatch.germination
            )

            // Iterate and apply bold style if the detail name matches the highlightTask
            details.forEachIndexed { index, (label, value) ->
                val isHighlighted = label.equals(highlightTask, ignoreCase = true)

                withStyle(
                    style = SpanStyle(
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                        color = Color(0xFF434231) // Keep the original color
                    )
                ) {
                    append("$label: $value")
                }

                // Add comma separator, but not after the last item
                if (index < details.lastIndex) {
                    append(", ")
                }
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() } // Click effect
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF10F)),
        modifier = Modifier
            .clickable( // Add click effect
                interactionSource = interactionSource,
                indication = ripple(color = Color.White), // Customize ripple color
                onClick = onClick
            )
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // The image on the left side of the card
            Image(
                // Replace with your actual image resource
                painter = painterResource(id = R.drawable.seed_image_sample_1),
                contentDescription = "Seed Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            )

            // Add some space between the image and the text content
            Spacer(modifier = Modifier.width(16.dp))

            // Column to hold the title and description texts
            Column {
                // The title text (e.g., TDK1)
                Text(
                    text = seedBatchData?.varietyName ?: "Select Batch", // Display selected name
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // First line of details
                val detailsText = if (seedBatchData != null) {
                    // Use the new AnnotatedString function
                    getDetailText(seedBatchData)
                } else {
                    AnnotatedString(
                        "Year: N/A, Season: N/A, Grading: N/A, Generation: N/A, Germination: N/A"
                    )
                }
                Text(
                    text = detailsText,
                    fontSize = 10.sp,
                    color = Color(0xFF434231),
                    style = TextStyle(lineHeight = 14.sp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Second line of details
                if (seedBatchData != null) {
                    Text(
                        text = seedBatchData.stockDisplay, // Display selected stock
                        fontSize = 12.sp,
                        color = Color.Black,
                        style = TextStyle(lineHeight = 14.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun SeedBatchStockOutCard(
    seedBatchData: SeedBatchStockOutData? = null, // Data from SelectSeedBatchScreen
    onCardClicked: () -> Unit = {}, // Add an onClick lambda parameter for click effect
    onDeleteClicked: () -> Unit = {}, // Lambda for the delete action
    highlightTask: String? = null,
    isSell: Boolean = true
) {
    // Helper function moved inside or defined externally if preferred
    fun getDetailText(seedBatch: SeedBatchStockOutData): AnnotatedString {
        return buildAnnotatedString {
            // Define all parts of the string
            val details = listOf(
                "Year" to seedBatch.year,
                "Season" to seedBatch.season,
                "Grading" to seedBatch.grading,
                "Generation" to seedBatch.generation,
                "Germination" to seedBatch.germination,
            )

            // Iterate and apply bold style if the detail name matches the highlightTask
            details.forEachIndexed { index, (label, value) ->
                val isHighlighted = label.equals(highlightTask, ignoreCase = true)

                withStyle(
                    style = SpanStyle(
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                        color = Color(0xFF434231) // Keep the original color
                    )
                ) {
                    append("$label: $value")
                }

                // Add comma separator, but not after the last item
                if (index < details.lastIndex) {
                    append(", ")
                }
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() } // Click effect
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF10F)),
        modifier = Modifier
            .clickable( // Add click effect
                interactionSource = interactionSource,
                indication = ripple(color = Color.White), // Customize ripple color
                onClick = onCardClicked
            )
            .padding(8.dp),
    ) {
        Box( // Box is for adding trash icon overlay
            modifier = Modifier
                .fillMaxWidth() // Box must fill width of the Card
            // Card click behavior is applied to the content Row inside the Box
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // The image on the left side of the card
                Image(
                    // Replace with your actual image resource
                    painter = painterResource(id = R.drawable.seed_image_sample_1),
                    contentDescription = "Seed Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                )

                // Add some space between the image and the text content
                Spacer(modifier = Modifier.width(16.dp))

                // Column to hold the title and description texts
                Column {
                    // The title text (e.g., TDK1)
                    Text(
                        text = (seedBatchData?.varietyName ?: "Select Batch") +
                                (seedBatchData?.seedBatchId?.let { " (Batch#$it)" } ?: ""),// Display selected name
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // First line of details
                    val detailsText = if (seedBatchData != null) {
                        // Use the new AnnotatedString function
                        getDetailText(seedBatchData)
                    } else {
                        AnnotatedString(
                            "Year: N/A, Season: N/A, Grading: N/A, Generation: N/A, Germination: N/A"
                        )
                    }
                    Text(
                        text = detailsText,
                        fontSize = 10.sp,
                        color = Color(0xFF434231),
                        style = TextStyle(lineHeight = 14.sp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Second line of details
                    if (seedBatchData != null) {
                        // Prevent long decimal places e.g. make 124.4499999999999999999 to 124.45
                        val roundedAvailableStock = round(seedBatchData.totalStock * 100.0) / 100.0
                        val quantityDisplay = "Quantity: ${seedBatchData.quantity} Kg (Stock: $roundedAvailableStock Kg)"
                        Text(
                            text = (quantityDisplay),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            style = TextStyle(lineHeight = 14.sp)
                        )

                        if (isSell) {
                            Text(
                                text = (
                                        "Price: " + seedBatchData.priceDisplay
                                        ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                style = TextStyle(lineHeight = 14.sp)
                            )
                        }
                    }
                }
            }
            // 2. Trash Icon Button placed inside the Box
            // Crucial: Use align modifier to position it at the bottom-right of the Box
            TrashIconButton(
                onClick = onDeleteClicked, // Use the new onDeleteClick lambda
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Positions the button
                    .padding(end = 0.dp, bottom = 0.dp) // Fine-tune position with small padding
            )
        }
    }
}

@Composable
fun SeedBatchStockInCard(
    seedBatchData: SeedBatchStockInData? = null, // Data from SelectSeedBatchScreen
    onCardClicked: () -> Unit = {}, // Add an onClick lambda parameter for click effect
    onDeleteClicked: () -> Unit = {}, // Lambda for the delete action
    highlightTask: String? = null,
    isPurchase: Boolean = true
) {
    // Helper function moved inside or defined externally if preferred
    fun getDetailText(seedBatch: SeedBatchStockInData): AnnotatedString {
        return buildAnnotatedString {
            // Define all parts of the string
            val details = listOf(
                "Year" to seedBatch.year,
                "Season" to seedBatch.season,
                "Grading" to seedBatch.grading,
                "Generation" to seedBatch.generation,
                "Germination" to seedBatch.germination
            )

            // Iterate and apply bold style if the detail name matches the highlightTask
            details.forEachIndexed { index, (label, value) ->
                val isHighlighted = label.equals(highlightTask, ignoreCase = true)

                withStyle(
                    style = SpanStyle(
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                        color = Color(0xFF434231) // Keep the original color
                    )
                ) {
                    append("$label: $value")
                }

                // Add comma separator, but not after the last item
                if (index < details.lastIndex) {
                    append(", ")
                }
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() } // Click effect
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF10F)),
        modifier = Modifier
            .clickable( // Add click effect
                interactionSource = interactionSource,
                indication = ripple(color = Color.White), // Customize ripple color
                onClick = onCardClicked
            )
            .padding(8.dp),
    ) {
        Box( // Box is for adding trash icon overlay
            modifier = Modifier
                .fillMaxWidth() // Box must fill width of the Card
            // Card click behavior is applied to the content Row inside the Box
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // The image on the left side of the card
                Image(
                    // Replace with your actual image resource
                    painter = painterResource(id = R.drawable.seed_image_sample_1),
                    contentDescription = "Seed Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                )

                // Add some space between the image and the text content
                Spacer(modifier = Modifier.width(16.dp))

                // Column to hold the title and description texts
                Column {
                    // The title text (e.g., TDK1)
                    Text(
                        text = seedBatchData?.varietyName
                            ?: "Select Batch", // Display selected name
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // First line of details
                    val detailsText = if (seedBatchData != null) {
                        // Use the new AnnotatedString function
                        getDetailText(seedBatchData)
                    } else {
                        AnnotatedString(
                            "Year: N/A, Season: N/A, Grading: N/A, Generation: N/A, Germination: N/A"
                        )
                    }
                    Text(
                        text = detailsText,
                        fontSize = 10.sp,
                        color = Color(0xFF434231),
                        style = TextStyle(lineHeight = 14.sp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Second line of details
                    if (seedBatchData != null) {
                        // Prevent long decimal places e.g. make 124.4499999999999999999 to 124.45
                        val roundedAvailableStock = round(seedBatchData.totalStock * 100.0) / 100.0

                        val quantityDisplay = if (seedBatchData.totalStock < 0)
                            "Quantity: ${seedBatchData.quantity} Kg (to create new batch)"
                        else
                            "Quantity: ${seedBatchData.quantity} Kg (Stock: $roundedAvailableStock Kg)"

                        Text(
                            text = (quantityDisplay),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            style = TextStyle(lineHeight = 14.sp)
                        )

                        if (isPurchase) {
                            Text(
                                text = (
                                        "Price: " + seedBatchData.priceDisplay
                                        ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                style = TextStyle(lineHeight = 14.sp)
                            )
                        }
                    }
                }
            }
            // 2. Trash Icon Button placed inside the Box
            // Crucial: Use align modifier to position it at the bottom-right of the Box
            TrashIconButton(
                onClick = onDeleteClicked, // Use the new onDeleteClick lambda
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Positions the button
                    .padding(end = 0.dp, bottom = 0.dp) // Fine-tune position with small padding
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeedBatchCard() {
    RCRCSeedManagerTheme {
        SeedBatchCard()
    }
}

// -- Stock In Card --
@Preview(showBackground = true)
@Composable
fun PreviewSeedBatchStockInCard() {
    RCRCSeedManagerTheme {
        SeedBatchStockInCard()
    }
}

// -- Stock Out Card --
@Preview(showBackground = true)
@Composable
fun PreviewSeedBatchStockOutCard(){
    RCRCSeedManagerTheme {
        SeedBatchStockOutCard()
    }
}