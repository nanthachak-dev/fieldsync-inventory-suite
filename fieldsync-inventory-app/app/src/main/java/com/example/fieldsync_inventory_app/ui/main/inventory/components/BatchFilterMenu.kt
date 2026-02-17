package com.example.fieldsync_inventory_app.ui.main.inventory.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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

@Composable
fun BatchFilterMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    activeFilter: String,
    onFilterChange: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF4A493E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Text(
            "Filter By",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

        FilterMenuItem("None", activeFilter) { onFilterChange("None") }
        
        SectionHeader("Generation")
        FilterMenuItem("R1", activeFilter) { onFilterChange("R1") }
        FilterMenuItem("R2", activeFilter) { onFilterChange("R2") }
        FilterMenuItem("R3", activeFilter) { onFilterChange("R3") }
        
        SectionHeader("Status")
        FilterMenuItem("Graded", activeFilter) { onFilterChange("Graded") }
        FilterMenuItem("Ungraded", activeFilter) { onFilterChange("Ungraded") }
        FilterMenuItem("Germinated", activeFilter) { onFilterChange("Germinated") }
        FilterMenuItem("Ungerminated", activeFilter) { onFilterChange("Ungerminated") }
    }
}

@Composable
private fun SectionHeader(label: String) {
    Text(
        text = label,
        color = Color(0xFFFBC02D).copy(alpha = 0.6f),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun FilterMenuItem(
    label: String,
    currentFilter: String,
    onClick: () -> Unit
) {
    val isSelected = label == currentFilter
    val accentColor = Color(0xFFFBC02D)

    DropdownMenuItem(
        text = {
            Text(
                label,
                color = if (isSelected) accentColor else Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        onClick = onClick,
        trailingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        colors = MenuDefaults.itemColors(
            textColor = Color.White,
        )
    )
}
