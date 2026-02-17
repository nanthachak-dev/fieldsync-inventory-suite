package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DateInput(
    label: String,
    selectedDate: String,
    modifier: Modifier = Modifier,
    launchDialogs: () -> Unit
) {
    // Wrap the OutlinedTextField in a Box to intercept clicks on the TextField
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { /* Read-only field */ },
            readOnly = true,
            modifier = modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFF0E68C),
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color(0xFFF0E68C),
                focusedContainerColor = Color(0xFF6C635B),
                unfocusedContainerColor = Color(0xFF6C635B),
                focusedLabelColor = Color(0xFFF0E68C),
                unfocusedLabelColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            label = { Text(text = label) },
            trailingIcon = { Text(text = "🗓️") },
        )
        // This invisible Box covers the entire TextField and intercepts the click
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = launchDialogs)
        )
    }
}

// -- Preview --
@Preview(showBackground = true)
@Composable
private fun  PreviewDateInput(){
    DateInput(
        label = "Date",
        selectedDate = "2023-Dec-21",
        modifier = Modifier,
        launchDialogs = {}
    )
}