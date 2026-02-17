package com.example.fieldsync_inventory_app.domain.repository.stock

import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.domain.model.stock.VarietySummaryPaged

interface StockRepository {
    suspend fun getStockSummary(lastDate: String? = null): StockSummary
    suspend fun getVarietySummary(lastDate: String? = null, page: Int? = null, size: Int? = null): VarietySummaryPaged
}
