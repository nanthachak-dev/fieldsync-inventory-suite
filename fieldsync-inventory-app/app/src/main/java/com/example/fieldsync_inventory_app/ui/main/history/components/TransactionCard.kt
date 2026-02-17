package com.example.fieldsync_inventory_app.ui.main.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.ui.main.history.model.TransactionCardData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun TransactionCard(
    data: TransactionCardData? = null,
    onClick: () -> Unit = {},
    //onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6C635B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        if (data != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Define icon color based on transaction type
                Icon(
                    painter = painterResource(id = data.transactionTypeSymbol),
                    contentDescription = "Manage Stock",
                    Modifier.height(20.dp),
                    tint = if (data.summary.totalSalePrice != null) Color.Yellow
                    else if (data.summary.totalPurchasePrice != null) Color(0xFF78BBF8)
                    else Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "[Transaction #${data.summary.transactionId}]",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "By: ${data.summary.username}",
                            color = Color(0xFFB7B03A),
                            fontSize = 12.sp
                        )
                    }
                    // Transaction details
                    Text(
                        text = data.transactionDescription,
                        color = Color.White,
                        fontSize = 14.sp,
                    )
                    // Transaction description
                    data.summary.transactionDescription?.let {
                        Text(
                            text = it,
                            color = Color.LightGray,
                            fontSize = 12.sp,
                        )
                    }
                    // Supplier and customer names
                    data.summary.supplierName?.let {
                        Text(
                            text = "Supplier: $it",
                            color = Color(0xFFB7B03A),
                            fontSize = 12.sp,
                        )
                    }
                    data.summary.customerName?.let {
                        Text(
                            text = "Customer: $it",
                            color = Color(0xFFB7B03A),
                            fontSize = 12.sp,
                        )
                    }

                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = data.transactionDate,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
//                        Icon(
//                            imageVector = Icons.Default.Edit,
//                            contentDescription = "Edit",
//                            tint = Color.White,
//                            modifier = Modifier
//                                .clickable { onEdit() }
//                                .padding(4.dp)
//                                .height(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFF87171),
                            modifier = Modifier
                                .clickable { onDelete() }
                                .padding(4.dp)
                                .height(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTransactionItem() {
    val summary = StockTransactionSummary(
        transactionId = 123456L,
        transactionDate = System.currentTimeMillis() - 7200000,
        transactionTypeId = 1,
        transactionTypeName = "STOCK_OUT",
        transactionDescription = "Sold 2 items for 26.0 kg",
        username = "user",
        mainMovementType = "SALE",
        itemCount = 2,
        totalQuantity = 26.0,
        totalSalePrice = 2200000.0,
        totalPurchasePrice = null,
        customerName = "John Doe",
        supplierName = null,
        fromBatchId = null,
        toBatchId = null
    )


    RCRCSeedManagerTheme {
        TransactionCard(data = TransactionCardData(summary))
    }
}
