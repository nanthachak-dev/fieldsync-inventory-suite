package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.ui.common.components.TrashIconButton

@Composable
fun RiceVarietyCard(
    variety: RiceVariety? =null,
    onCardClicked: () -> Unit = {}, // Add an onClick lambda parameter for click effect
    onDeleteClicked: () -> Unit = {}, // Lambda for the delete action
){
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
                // Add some space between the image and the text content
                Spacer(modifier = Modifier.width(16.dp))

                // Column to hold the title and description texts
                Column {
                    // The title text (e.g., TDK1)

                    Text(
                        text = (variety?.name?:"RX-93") + (" [id #${variety?.id?:"93"}]"), // If null, use default value
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Field data
                    Text(
                        text = variety?.description?:"RX-93",
                        fontSize = 10.sp,
                        color = Color(0xFF434231),
                        style = TextStyle(lineHeight = 14.sp)
                    )
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

// -- Preview --
@Preview(showBackground = true)
@Composable
fun PreviewRiceVarietyCard(){
    RiceVarietyCard()
}