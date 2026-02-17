package com.example.fieldsync_inventory_app.ui.main.inventory.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddToSaleDialog(
    showDialog: Boolean,
    batchCard: BatchCardData?,
    onDismiss: () -> Unit,
    onConfirm: (quantity: String, price: String) -> Unit
) {
    // Variables that hold value of text fields
    var currentQuantity by remember { mutableStateOf("") }
    var currentPrice by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                currentQuantity = ""
                currentPrice = ""
                onDismiss()
            },
            containerColor = Color(0xFF3D3C31),
            title = {
                Text(text = "Add to Sale - Batch #${batchCard?.batchId}", color = Color.White)
            },
            text = {
                Column {
                    // Quantity text field
                    InventoryNumericTextField(
                        value = currentQuantity,
                        onValueChange = { currentQuantity = it },
                        label = "Quantity (Kg)"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Price text field
                    InventoryNumericTextField(
                        value = currentPrice,
                        onValueChange = { currentPrice = it },
                        label = "Price per Kg"
                    )
                }
            },
            confirmButton = {
                // Add button
                TextButton(
                    onClick = {
                        onConfirm(currentQuantity, currentPrice)
                        currentQuantity = ""
                        currentPrice = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text("Add", color = Color.White)
                }
            },
            dismissButton = {
                // Cancel button
                TextButton(
                    onClick = {
                        currentQuantity = ""
                        currentPrice = ""
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}

// Need to add 2 of @Preview as a trick for preview to work
// else the preview is not shown because tex property of AlertDialog doesn't work with
// OutlinedTextField in preview
@Preview(showBackground = true)
@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun PreviewAddToSaleDialog() {
    RCRCSeedManagerTheme {
        AddToSaleDialog(
            showDialog = true,
            batchCard = BatchCardData(
                batchId = 1L,
                varietyName = "Sample Variety",
                seasonName = "Sample Season",
                year = 2023,
                stock = 50.0,
                varietyId = 1,
                seasonId = 1,
                seasonDescription = "Sample Season Description",
                generationId = 1,
                generationName = "Sample Generation",
                grading = false,
                germination = false
            ),
            onDismiss = {},
            onConfirm = { _, _ -> }
        )
    }
}
