package com.example.fieldsync_inventory_app.ui.main.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionDetailDialog(
    summary: StockTransactionSummary,
    details: List<StockMovementDetails>,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4A443A)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "[Transaction #${summary.transactionId}]",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 500.dp) // Flexible but not exceed max high
                    ) {
                        items(details) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF5E5A50)
                                )
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    val grading =
                                        if (item.seedBatchGrading) "Graded" else "Ungraded"
                                    val germination =
                                        if (item.seedBatchGermination) "Germinated" else "Ungerminated"
                                    var price = ""
                                    if (item.saleItemPrice != null)
                                        price =
                                            " at ${item.saleItemPrice} Kip/Kg for ${
                                                numberFormat.format(
                                                    item.stockMovementQuantity * item.saleItemPrice
                                                )
                                            } Kip"
                                    else if (item.purchaseItemPrice != null)
                                        price =
                                            " at ${item.purchaseItemPrice} Kip/Kg for ${
                                                numberFormat.format(
                                                    item.stockMovementQuantity * item.purchaseItemPrice
                                                )
                                            } Kip"

                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("Batch #${item.seedBatchId} : ${item.riceVarietyName}")
                                            }
                                            append("\n[${item.seedBatchYear} | ${item.seasonDescription} | ${item.generationName} | $grading | $germination] of ${item.stockMovementQuantity} Kg$price")
                                        },
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // -- Show Totals --
                    if (summary.transactionTypeName != "ADJUSTMENT") {
                        // Total items
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Items:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${summary.itemCount}", color = Color.White)
                        }
                        // Total stock
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Stock:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${summary.totalQuantity} Kg", color = Color.White)
                        }
                    } else {
                        // Transfer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Transferred:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${details[0].stockMovementQuantity} Kg", color = Color.White)
                        }
                        // Loss
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Loss:", fontWeight = FontWeight.Bold, color = Color.White)
                            val loss =
                                details[0].stockMovementQuantity - details[1].stockMovementQuantity
                            Text("$loss Kg", color = Color.White)
                        }
                    }


                    summary.totalSalePrice?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Price:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${numberFormat.format(it)} LAK", color = Color.White)
                        }
                    }

                    summary.totalPurchasePrice?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Price:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${numberFormat.format(it)} LAK", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
