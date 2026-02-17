package com.example.fieldsync_inventory_app.ui.common.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

@Composable
fun DateTimeInput(
    label: String,
    selectedDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var tempDate by remember { mutableStateOf(selectedDateTime.toLocalDate()) }

    // --- 1. Define the "Now" Action ---
    val setTimeNow: () -> Unit = {
        // Truncate to minutes for consistency
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        onDateTimeSelected(now)
    }

    // --- Time Picker Dialog (No Change) ---
    val timePickerDialog = remember(selectedDateTime.hour, selectedDateTime.minute) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val newDateTime = tempDate.atTime(hour, minute)
                    .truncatedTo(ChronoUnit.MINUTES)
                onDateTimeSelected(newDateTime)
            },
            selectedDateTime.hour,
            selectedDateTime.minute,
            false
        )
    }

    // --- 2. Date Picker Dialog with "Now" Button Logic ---
    val datePickerDialog = remember(selectedDateTime.year,
        selectedDateTime.monthValue, selectedDateTime.dayOfMonth) {
        val dialog = DatePickerDialog(
            context,
            { _, year: Int, month: Int, dayOfMonth: Int ->
                // Positive click (OK) logic remains the same:
                tempDate = tempDate.withYear(year)
                    .withMonth(month + 1)
                    .withDayOfMonth(dayOfMonth)
                timePickerDialog.show()
            },
            selectedDateTime.year,
            selectedDateTime.monthValue - 1,
            selectedDateTime.dayOfMonth
        )

        // 3. Set the Negative Button text and action
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "NOW") { _, _ ->
            // This lambda is executed when the "NOW" button is clicked
            setTimeNow()
            // IMPORTANT: Since the "NOW" button is clicked, we stop the date/time selection flow.
        }

        // You could also use BUTTON_NEUTRAL if you want "CANCEL" and "NOW" to both appear.
        // dialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Cancel") { _, _ -> /* dismiss */ }

        dialog
    }

    val launchDialogs: () -> Unit = {
        tempDate = selectedDateTime.toLocalDate()
        datePickerDialog.show()
    }

    // --- OutlinedTextField Composable (Display and Click Handlers) ---
    OutlinedTextField(
        value = selectedDateTime.format(DATE_TIME_FORMATTER),
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

        label = {
            Text(
                text = label,
                modifier = Modifier.clickable(onClick = launchDialogs),
            )
        },

        trailingIcon = {
            Text(
                text = "🗓️",
                modifier = Modifier.clickable(onClick = launchDialogs)
            )
        }
    )
}