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
fun CompoBoxCustom(
    list: List<String>,
    selectedItem: String?, // State is passed in
    startIndex: Int,
    label: String,
    onItemSelected: (String) -> Unit, // Callback for selection change with selected item
    modifier: Modifier = Modifier
) {
    // State variable to track whether the dropdown menu is expanded or not.
    var isExpanded by remember { mutableStateOf(false) }

    // State variable to hold the currently selected item.
    var selectedText by remember { mutableStateOf<String?>(null) }

    // If startIndex <0 means that selected item is empty
    if (startIndex >= 0) {
        selectedText = list[startIndex]
    }

    // Use a Box to give the ComboBox some padding.
    Box(
        modifier = modifier
    ) {
        // ExposedDropdownMenuBox is the Material 3 component for creating a ComboBox.
        // It manages the state and provides the content slots.
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            // The TextField acts as the visible button part of the ComboBox.
            // It displays the currently selected item and handles the click to expand the menu.
            TextField(
                // The menuAnchor modifier is essential for correct positioning.
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, true),
                readOnly = true, // The user can't manually type in the field.
                // Display the selected item passed from the parent.
                value = selectedItem.orEmpty(), // Use the passed-in state
                onValueChange = {}, // This is empty because it's readOnly.
                label = { Text(label) },
                // Use the built-in trailing icon provided by ExposedDropdownMenuDefaults.
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
                // Use the default colors for the text field.
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.DarkGray,
                    unfocusedLabelColor = Color.Gray
                )
            )

            // The dropdown menu that appears when the TextField is clicked.
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    // Close the menu when the user taps outside of it.
                    isExpanded = false
                },
                modifier = Modifier.background(Color.White)
            ) {
                // Iterate through the list of options and create a menu item for each.
                list.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(selectionOption, color = Color.DarkGray)

                        },
                        onClick = {
                            // Notify the parent of the new selection.
                            onItemSelected(selectionOption) // Call the callback
                            // Close the dropdown menu.
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}