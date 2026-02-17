package com.example.fieldsync_inventory_app.ui.main.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fieldsync_inventory_app.ui.common.components.FilterableComboBox
import com.example.fieldsync_inventory_app.ui.main.history.IHistoryViewModel
import com.example.fieldsync_inventory_app.ui.main.history.state.HistoryUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryFilterDialog(
    historyUiState: HistoryUiState,
    viewModel: IHistoryViewModel,
    onDismiss: () -> Unit
) {
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3D3C31)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // From date button
                    Button(
                        onClick = { showFromDatePicker = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                    ) {
                        Text(historyUiState.selectedFromDate ?: "From", color = Color.White)
                    }

                    // To date button
                    Button(
                        onClick = { showToDatePicker = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                    ) {
                        Text(historyUiState.selectedToDate ?: "To", color = Color.White)
                    }
                }


                // Task type (stock movement type)
                FilterableComboBox(
                    items = historyUiState.mainMovementTypeNicknames,
                    selectedValue = historyUiState.selectedMainMovementTypeNickname ?: "",
                    onValueChange = { viewModel.onMainMovementTypeNicknameChanged(it) },
                    label = "Task Type"
                )

                // Username
                FilterableComboBox(
                    items = historyUiState.usernames,
                    selectedValue = historyUiState.selectedUsername ?: "",
                    onValueChange = { viewModel.onUsernameChanged(it) },
                    label = "Username"
                )

                // Customer full name
                FilterableComboBox(
                    items = historyUiState.customers,
                    selectedValue = historyUiState.selectedCustomerName ?: "",
                    onValueChange = { viewModel.onCustomerNameChanged(it) },
                    label = "Customer Full Name"
                )

                // Supplier full name
                FilterableComboBox(
                    items = historyUiState.suppliers,
                    selectedValue = historyUiState.selectedSupplierName ?: "",
                    onValueChange = { viewModel.onSupplierNameChanged(it) },
                    label = "Supplier Full Name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Default button (Reset filter
                    TextButton(onClick = {
                        viewModel.clearFilters()
                        onDismiss()
                    }) {
                        Text("Default", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // OK button (Apply filter)
                    Button(
                        onClick = {
                            viewModel.applyFilters()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            }
        }
    }

    // -- Date picker (From) --
    // Set date value to UI state through view model's onFromDateChanged()
    if (showFromDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onFromDateChanged(SimpleDateFormat("yyyy-MM-dd",
                            Locale.getDefault()).format(Date(it)))
                    }
                    showFromDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFromDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // -- Date picker (To) --
    if (showToDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onToDateChanged(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)))
                    }
                    showToDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showToDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// -- Preview --
//@Preview(showBackground = true)
//@Composable
//private fun PreviewCustomFilterDialog() {
//    CustomFilterDialog(
//        HistoryUiState(),
//        PreviewHistoryViewModel()
//    ) { }
//}