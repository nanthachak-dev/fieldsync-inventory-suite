package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fieldsync_inventory_app.R

@Composable
fun TrashIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "Delete Item"
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            // Use the built-in Icons.Default.Delete (a trash can icon)
            imageVector = Icons.Default.Delete,
            // Provide a content description for accessibility
            contentDescription = contentDescription,
            // Optionally set the color to a destructive/error color for emphasis
            tint = Color.Gray,
        )
    }
}

@Composable
fun FilterIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "Filter",
    tint: Color = Color.Yellow,
    containerColor: Color = Color.Transparent
){
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor)
    ) {
        val iconSize = 20.dp
        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}

@Composable
fun SortIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "Sort",
    tint: Color = Color.Yellow,
    containerColor: Color = Color.Transparent
){
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor)
    ) {
        val iconSize = 20.dp
        Icon(
            painter = painterResource(id = R.drawable.ic_sort),
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}

@Preview
@Composable
fun TrashIconButtonPreview() {
    TrashIconButton(onClick = {})
}

@Preview
@Composable
fun FilterIconButtonPreview() {
    FilterIconButton(onClick = {})
}

@Preview
@Composable
fun SortIconButtonPreview() {
    SortIconButton(onClick = {})
}