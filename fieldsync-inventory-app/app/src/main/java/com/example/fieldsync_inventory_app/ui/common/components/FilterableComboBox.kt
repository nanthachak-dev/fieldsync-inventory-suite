package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterableComboBox(
    items: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember(selectedValue) { mutableStateOf(selectedValue) }

    val filteredItems = if (textFieldValue == selectedValue) {
        items
    } else {
        items.filter { it.contains(textFieldValue, ignoreCase = true) }
    }

    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    if (!expanded) {
                        expanded = true
                    }
                },
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.DarkGray,
                    unfocusedLabelColor = Color.Gray
                )
            )

            if (filteredItems.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    filteredItems.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(selectionOption, color = Color.DarkGray)
                            },
                            onClick = {
                                onValueChange(selectionOption)
                                textFieldValue = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}