package com.example.fieldsync_inventory_app.ui.main.inventory.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.fieldsync_inventory_app.util.number.ThousandSeparatorTransformation

/**
 * Validates if the input string is a valid decimal number with up to 2 decimal places
 * in US locale format (using period as decimal separator, no commas)
 */
private fun isValidDecimalInput(input: String): Boolean {
    if (input.isEmpty()) return true

    // Regex pattern for US locale decimal with max 2 decimal places (no commas in raw input)
    // Allows: empty, digits, digits with decimal point, digits with up to 2 decimal places
    val pattern = Regex("^\\d*\\.?\\d{0,2}$")
    return pattern.matches(input)
}

@Composable
fun InventoryNumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "0.00"
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only update if the new value is valid
            if (isValidDecimalInput(newValue)) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
        singleLine = true,
        placeholder = { Text(placeholder, color = Color.White) },
        visualTransformation = ThousandSeparatorTransformation,
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
    )
}
