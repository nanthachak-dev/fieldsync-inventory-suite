package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.StockTransactionSummaryPagedResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.StockTransactionSummaryResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.TotalStockResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.TotalTransactionResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.TotalSaleResponseDto
import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.domain.model.TotalStock
import com.example.fieldsync_inventory_app.domain.model.TotalTransaction
import com.example.fieldsync_inventory_app.domain.model.TotalSale
import java.time.Instant

fun StockTransactionSummaryResponseDto.toDomain(): StockTransactionSummary {
    return StockTransactionSummary(
        transactionId = this.transactionId,
        transactionDate = try { Instant.parse(this.transactionDate).toEpochMilli() } catch (e: Exception) { 0L },
        transactionTypeId = this.transactionTypeId,
        transactionTypeName = this.transactionTypeName,
        transactionDescription = this.transactionDescription,
        username = this.username,
        mainMovementType = this.mainMovementType,
        itemCount = this.itemCount,
        totalQuantity = this.totalQuantity,
        totalSalePrice = this.totalSalePrice,
        totalPurchasePrice = this.totalPurchasePrice,
        customerName = this.customerName,
        supplierName = this.supplierName,
        fromBatchId = this.fromBatchId,
        toBatchId = this.toBatchId
    )
}

fun StockTransactionSummaryPagedResponseDto.toDomain(): Page<StockTransactionSummary> {
    return Page(
        content = this.content.map { it.toDomain() },
        pageNumber = this.pageNumber,
        pageSize = this.pageSize,
        totalElements = this.totalElements,
        totalPages = this.totalPages,
        isLast = this.last
    )
}

fun TotalStockResponseDto.toDomain(): TotalStock {
    return TotalStock(
        totalStock = this.totalStock,
        asOfDate = try { Instant.parse(this.asOfDate).toEpochMilli() } catch (e: Exception) { 0L }
    )
}

fun TotalTransactionResponseDto.toDomain(): TotalTransaction {
    return TotalTransaction(
        totalTransactions = this.totalTransactions,
        startDate = try { Instant.parse(this.startDate).toEpochMilli() } catch (e: Exception) { 0L },
        endDate = try { Instant.parse(this.endDate).toEpochMilli() } catch (e: Exception) { 0L }
    )
}

fun TotalSaleResponseDto.toDomain(): TotalSale {
    return TotalSale(
        totalSale = this.totalSoldOut,
        startDate = try { Instant.parse(this.startDate).toEpochMilli() } catch (e: Exception) { 0L },
        endDate = try { Instant.parse(this.endDate).toEpochMilli() } catch (e: Exception) { 0L }
    )
}
