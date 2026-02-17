package com.example.fieldsync_inventory_app.ui.main.inventory.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData

@Composable
fun BatchContextMenu(
    expanded: Boolean,
    selectedBatchCard: BatchCardData?,
    currentBatchCard: BatchCardData,
    onDismissRequest: () -> Unit,
    onAddToAdjustStock: () -> Unit,
    onAddToSale: () -> Unit
) {
    DropdownMenu(
        expanded = expanded && selectedBatchCard?.batchId == currentBatchCard.batchId,
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF4A493E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(220.dp)
    ) {
        Text(
            "Batch Actions",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

        DropdownMenuItem(
            text = { 
                Text(
                    "Add to Adjust Stock", 
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ) 
            },
            onClick = onAddToAdjustStock,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFFFBC02D),
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = MenuDefaults.itemColors(textColor = Color.White)
        )
        DropdownMenuItem(
            text = { 
                Text(
                    "Add to Sale", 
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ) 
            },
            onClick = onAddToSale,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFF81C784),
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = MenuDefaults.itemColors(textColor = Color.White)
        )
    }
}
