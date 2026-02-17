package com.example.fieldsync_inventory_app.domain.repository.stock

import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.remote.api.StockApi
import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.domain.model.stock.VarietySummaryPaged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi
) : StockRepository {

    override suspend fun getStockSummary(lastDate: String?): StockSummary {
        return api.getStockSummary(lastDate).toDomain()
    }

    override suspend fun getVarietySummary(lastDate: String?, page: Int?, size: Int?): VarietySummaryPaged {
        return api.getVarietySummary(lastDate, page, size).toDomain()
    }
}
