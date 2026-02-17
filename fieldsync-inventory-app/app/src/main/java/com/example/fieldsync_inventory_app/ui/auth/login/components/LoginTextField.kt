package com.example.fieldsync_inventory_app.ui.auth.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun UserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String, // User
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
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

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String, // Password
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
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
    PasswordTextField(value = "", onValueChange = {}, label = "New Passcode")
}