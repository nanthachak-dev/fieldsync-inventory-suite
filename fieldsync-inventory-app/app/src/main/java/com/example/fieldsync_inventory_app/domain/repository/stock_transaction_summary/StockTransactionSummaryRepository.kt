package com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary

import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.domain.model.TotalStock
import com.example.fieldsync_inventory_app.domain.model.TotalTransaction
import com.example.fieldsync_inventory_app.domain.model.TotalSale

interface StockTransactionSummaryRepository {
    suspend fun getStockTransactionSummaries(
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Page<StockTransactionSummary>

    suspend fun getTotalStock(
        lastDate: String? = null
    ): TotalStock

    suspend fun getTotalTransactionCount(
        startDate: String? = null,
        endDate: String? = null
    ): TotalTransaction

    suspend fun getTotalSale(
        startDate: String? = null,
        endDate: String? = null
    ): TotalSale
}
