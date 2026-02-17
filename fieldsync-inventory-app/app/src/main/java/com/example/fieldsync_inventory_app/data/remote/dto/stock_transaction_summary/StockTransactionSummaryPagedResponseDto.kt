package com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary

data class StockTransactionSummaryPagedResponseDto(
    val content: List<StockTransactionSummaryResponseDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)
