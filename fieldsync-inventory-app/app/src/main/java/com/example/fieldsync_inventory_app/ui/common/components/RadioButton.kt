package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

// This is the main Composable function that displays the radio button group.
@Composable
fun RadioButtonGroupCustom(
    groupLabel: String?,
    radioItems: List<String>,
    selectedOption: String?, // State is passed in
    onOptionSelected: (String) -> Unit // Callback for selection change
) {
    // Column is a composable that arranges its children in a vertical sequence.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Add a group label for the radio buttons.
        if (groupLabel != null) {
            Text(
                text = groupLabel,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.White
            )
        }

        // The container is now a Row, arranging the items horizontally.
        Row(
            modifier = Modifier.selectableGroup(), // Tag the Row as a selectable group.
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Iterate through each option to create a radio button and a label for it.
            radioItems.forEach { text ->
                // Use a Row to place the RadioButton and Text side-by-side.
                Row(
                    Modifier
                        .selectable(
                            selected = (text == selectedOption), // Use the passed-in state
                            onClick = {
                                // Notify the parent of the new selection.
                                onOptionSelected(text) // Call the callback
                            }
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption), // Use the passed-in state
                        onClick = {
                            // Calling the provided callback.
                            onOptionSelected(text) // Call the callback
                        },
                        colors = RadioButtonDefaults.colors(
                            // Set the color of the radio button when it is selected.
                            selectedColor = Color(0xFF24A0ED),
                            // You can also set the color when it is not selected.
                            unselectedColor = Color.White
                        )
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 0.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}