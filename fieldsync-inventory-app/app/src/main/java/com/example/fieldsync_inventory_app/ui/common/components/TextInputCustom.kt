package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun isValidDecimalInput(input: String): Boolean {
    if (input.isEmpty()) return true

    // Regex pattern for US locale decimal with max 2 decimal places (no commas in raw input)
    // Allows: empty, digits, digits with decimal point, digits with up to 2 decimal places
    val pattern = Regex("^\\d*\\.?\\d{0,2}$")
    return pattern.matches(input)
}
@Composable
fun TextInputWithSuffix(
    label: String,
    suffix: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only update if the new value is valid
            if (isValidDecimalInput(newValue)) {
                onValueChange(newValue)
            }
        },
        modifier = modifier,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        placeholder = { Text("0.00") },
        trailingIcon = {
            Text(text = suffix)
        },
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            // Set the background color of the container to white
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,

            // Set the color for the text inside the text field to gray
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray,

            // Set the color for the label to gray
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,

            // Set the color for the trailing icon to gray
            focusedTrailingIconColor = Color.Gray,
            unfocusedTrailingIconColor = Color.Gray
        )
    )
}

@Composable
fun TextInputSimple(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textSize: Int = 16,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Gray,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label, fontSize = textSize.sp) },
        textStyle = TextStyle(fontSize = textSize.sp, color = contentColor),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        leadingIcon = leadingIcon,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedTextColor = contentColor,
            unfocusedTextColor = contentColor,
            focusedLabelColor = contentColor.copy(alpha = 0.7f),
            unfocusedLabelColor = contentColor.copy(alpha = 0.7f),
            focusedBorderColor = contentColor.copy(alpha = 0.5f),
            unfocusedBorderColor = contentColor.copy(alpha = 0.3f),
        ),
    )
}
