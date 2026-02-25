package com.example.fieldsync_inventory_api.enums;

/**
 * Task of transaction operation, comes with transaction operation POST
 */
public enum OperationTask {
    ADJUSTMENT,
    SALE, // Comes with request endpoint: transaction-operations/stock-in
    PURCHASE,
    TRANSFER_IN,
    TRANSFER_OUT,
    INITIAL_STOCK
}
