package com.example.fieldsync_inventory_app.ui.main.history

import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.ui.main.history.model.TransactionCardData

val sampleTransactions = listOf(
    TransactionCardData(
        StockTransactionSummary(
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
    ),
    TransactionCardData(
        StockTransactionSummary(
            transactionId = 3222L,
            transactionDate = System.currentTimeMillis() - 14400000,
            transactionTypeId = 3,
            transactionTypeName = "ADJUSTMENT",
            transactionDescription = "Adjusted 2 items for 250.0 kg",
            username = "user",
            mainMovementType = "ADJUSTMENT",
            itemCount = 2,
            totalQuantity = 250.0,
            totalSalePrice = null,
            totalPurchasePrice = null,
            customerName = null,
            supplierName = null,
            fromBatchId = 352L,
            toBatchId = 771L
        )
    )
)