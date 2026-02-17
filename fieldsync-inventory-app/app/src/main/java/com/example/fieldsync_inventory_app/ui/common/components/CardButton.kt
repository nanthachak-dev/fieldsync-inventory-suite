package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp


@Composable
fun CardButton(
    title: String,
    titleDescription: String,
    onClick: () -> Unit = {} // Add an onClick lambda parameter for click effect
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7C5B40)),
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp)
            .height(80.dp)
            .clickable( // Add click effect
                interactionSource = interactionSource,
                indication = ripple(color = Color.White), // Customize ripple color
                onClick = onClick
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp) // Not need to set when put in LazyVerticalGrid
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 1.em,
                textAlign = TextAlign.Center
            )
            Text(
                text = titleDescription,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 8.sp,
                lineHeight = 1.em,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SimpleCardButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // Add an onClick lambda parameter for click effect
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Yellow),
        modifier = modifier
            .height(80.dp)
            .clickable( // Add click effect
                interactionSource = interactionSource,
                indication = ripple(color = Color.White), // Customize ripple color
                onClick = onClick
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp) // Not need to set when put in LazyVerticalGrid
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 1.em,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun CardButtonPreview() {
    CardButton("Title", "Title Description")
}