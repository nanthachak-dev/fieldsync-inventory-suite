package com.example.fieldsync_inventory_app.ui.auth.passcode.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PasscodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String, // New Passcode, Confirm Passcode, etc.
){
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            // Accept only digits and max 4 characters
            if (newText.all { it.isDigit() } && newText.length <= 4) {
                onValueChange(newText)
            }
        },
        label = {
            Text(
                text = label,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
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
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPasscodeTextField() {
    PasscodeTextField(value = "", onValueChange = {}, label = "New Passcode")
}