package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.inventory.BatchStockDto
import com.example.fieldsync_inventory_app.data.remote.dto.inventory.InventoryBatchResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.inventory.InventoryResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.inventory.VarietyStockDto
import com.example.fieldsync_inventory_app.domain.model.inventory.BatchStock
import com.example.fieldsync_inventory_app.domain.model.inventory.Inventory
import com.example.fieldsync_inventory_app.domain.model.inventory.InventoryBatch
import com.example.fieldsync_inventory_app.domain.model.inventory.VarietyStock

fun InventoryResponseDto.toDomain(): Inventory {
    return Inventory(
        totalStock = this.totalStock,
        totalGraded = this.totalGraded,
        totalUngraded = this.totalUngraded,
        totalGerminated = this.totalGerminated,
        totalUngerminated = this.totalUngerminated,
        varieties = this.varieties.map { it.toDomain() }
    )
}

fun VarietyStockDto.toDomain(): VarietyStock {
    return VarietyStock(
        varietyId = this.varietyId,
        varietyName = this.varietyName,
        stock = this.stock,
        graded = this.graded,
        ungraded = this.ungraded,
        germinated = this.germinated,
        ungerminated = this.ungerminated
    )
}

fun InventoryBatchResponseDto.toDomain(): InventoryBatch {
    return InventoryBatch(
        totalStock = this.totalStock,
        totalGraded = this.totalGraded,
        totalUngraded = this.totalUngraded,
        totalGerminated = this.totalGerminated,
        totalUngerminated = this.totalUngerminated,
        batches = this.batches.map { it.toDomain() }
    )
}

fun BatchStockDto.toDomain(): BatchStock {
    return BatchStock(
        batchId = this.batchId,
        varietyId = this.varietyId,
        varietyName = this.varietyName,
        year = this.year,
        seasonId = this.seasonId,
        seasonName = this.seasonName,
        seasonDescription = this.seasonDescription,
        generationId = this.generationId,
        generationName = this.generationName,
        grading = this.grading,
        germination = this.germination,
        stock = this.stock
    )
}
