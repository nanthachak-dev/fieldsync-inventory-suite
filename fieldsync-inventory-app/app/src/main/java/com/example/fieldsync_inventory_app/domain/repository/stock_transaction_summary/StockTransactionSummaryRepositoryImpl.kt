package com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary

import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.remote.api.StockTransactionSummaryApi
import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.domain.model.TotalStock
import com.example.fieldsync_inventory_app.domain.model.TotalTransaction
import com.example.fieldsync_inventory_app.domain.model.TotalSale
import javax.inject.Inject

class StockTransactionSummaryRepositoryImpl @Inject constructor(
    private val api: StockTransactionSummaryApi
) : StockTransactionSummaryRepository {
    override suspend fun getStockTransactionSummaries(
        page: Int?,
        size: Int?,
        sort: String?,
        startDate: String?,
        endDate: String?
    ): Page<StockTransactionSummary> {
        return api.getStockTransactionSummaries(
            page = page,
            size = size,
            sort = sort,
            startDate = startDate,
            endDate = endDate
        ).toDomain()
    }

    override suspend fun getTotalStock(lastDate: String?): TotalStock {
        return api.getTotalStock(lastDate).toDomain()
    }

    override suspend fun getTotalTransactionCount(
        startDate: String?,
        endDate: String?
    ): TotalTransaction {
        return api.getTotalTransactionCount(startDate, endDate).toDomain()
    }

    override suspend fun getTotalSale(startDate: String?, endDate: String?): TotalSale {
        return api.getTotalSale(startDate, endDate).toDomain()
    }
}
