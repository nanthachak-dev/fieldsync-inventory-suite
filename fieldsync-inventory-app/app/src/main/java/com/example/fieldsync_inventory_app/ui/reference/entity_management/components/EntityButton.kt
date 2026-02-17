package com.example.fieldsync_inventory_app.ui.reference.entity_management.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EntityButton(
    modifier: Modifier = Modifier,
    mainText: String,
    subText: String?=null,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main text aligned to the top-center (default Box alignment)
            Text(
                text = mainText,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )

            // Sub text aligned to the bottom-end (bottom-right)
            if (subText != null) {
                Text(
                    text = subText,
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEntityButton() {
    EntityButton(
        mainText = "Manage Variety",
        onClick = {}
    )
}

