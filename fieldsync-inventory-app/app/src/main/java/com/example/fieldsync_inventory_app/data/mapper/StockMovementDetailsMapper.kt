package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.local.entity.StockMovementDetailsEntity
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.RiceVarietyStockResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.RiceVarietyStockPagedResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.StockMovementDetailsResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.TopSellingVarietyResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.TopSellingVarietyPagedResponseDto
import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.RiceVarietyStock
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.model.TopSellingVariety
import java.time.Instant

// -- Mapper --

// To map from API response DTO to Entity
fun StockMovementDetailsResponseDto.toEntity(): StockMovementDetailsEntity {
    return StockMovementDetailsEntity(
        transactionId = this.transactionId,
        stockMovementId = this.stockMovementId,
        transactionTypeId = this.transactionTypeId,
        transactionTypeName = this.transactionTypeName,
        userId = this.userId,
        username = this.username,
        transactionDate = try { Instant.parse(this.transactionDate).toEpochMilli() } catch (e: Exception) { 0L },
        transactionDescription = this.transactionDescription,
        movementTypeId = this.movementTypeId,
        movementTypeName = this.movementTypeName,
        movementTypeEffectOnStock = this.movementTypeEffectOnStock,
        movementTypeDescription = this.movementTypeDescription,
        stockMovementQuantity = this.stockMovementQuantity,
        stockMovementDescription = this.stockMovementDescription,
        seedBatchId = this.seedBatchId,
        seedBatchYear = this.seedBatchYear,
        seedBatchGrading = this.seedBatchGrading,
        seedBatchGermination = this.seedBatchGermination,
        seedBatchDescription = this.seedBatchDescription,
        riceVarietyId = this.riceVarietyId,
        riceVarietyName = this.riceVarietyName,
        riceVarietyDescription = this.riceVarietyDescription,
        riceVarietyImageUrl = this.riceVarietyImageUrl,
        generationId = this.generationId,
        generationName = this.generationName,
        generationDescription = this.generationDescription,
        seasonId = this.seasonId,
        seasonName = this.seasonName,
        seasonDescription = this.seasonDescription,
        saleId = this.saleId,
        customerId = this.customerId,
        customerFullName = this.customerFullName,
        saleDescription = this.saleDescription,
        saleItemPrice = this.saleItemPrice,
        saleItemDescription = this.saleItemDescription,
        purchaseId = this.purchaseId,
        purchaseDescription = this.purchaseDescription,
        supplierId = this.supplierId,
        supplierFullName = this.supplierFullName,
        purchaseItemPrice = this.purchaseItemPrice,
        purchaseItemDescription = this.purchaseItemDescription,
        createdAt = try { Instant.parse(this.createdAt).toEpochMilli() } catch (e: Exception) { 0L },
        updatedAt = try { Instant.parse(this.updatedAt).toEpochMilli() } catch (e: Exception) { 0L },
        deletedAt = this.deletedAt?.let { try { Instant.parse(it).toEpochMilli() } catch (e: Exception) { null } }
    )
}

// To map from DTO to Domain Model
fun StockMovementDetailsResponseDto.toDomain(): StockMovementDetails {
    return StockMovementDetails(
        transactionId = this.transactionId,
        stockMovementId = this.stockMovementId,
        transactionTypeId = this.transactionTypeId,
        transactionTypeName = this.transactionTypeName,
        userId = this.userId,
        username = this.username,
        transactionDate = try { Instant.parse(this.transactionDate).toEpochMilli() } catch (e: Exception) { 0L },
        transactionDescription = this.transactionDescription,
        movementTypeId = this.movementTypeId,
        movementTypeName = this.movementTypeName,
        movementTypeEffectOnStock = this.movementTypeEffectOnStock,
        movementTypeDescription = this.movementTypeDescription,
        stockMovementQuantity = this.stockMovementQuantity,
        stockMovementDescription = this.stockMovementDescription,
        seedBatchId = this.seedBatchId,
        seedBatchYear = this.seedBatchYear,
        seedBatchGrading = this.seedBatchGrading,
        seedBatchGermination = this.seedBatchGermination,
        seedBatchDescription = this.seedBatchDescription,
        riceVarietyId = this.riceVarietyId,
        riceVarietyName = this.riceVarietyName,
        riceVarietyDescription = this.riceVarietyDescription,
        riceVarietyImageUrl = this.riceVarietyImageUrl,
        generationId = this.generationId,
        generationName = this.generationName,
        generationDescription = this.generationDescription,
        seasonId = this.seasonId,
        seasonName = this.seasonName,
        seasonDescription = this.seasonDescription,
        saleId = this.saleId,
        customerId = this.customerId,
        customerFullName = this.customerFullName,
        saleDescription = this.saleDescription,
        saleItemPrice = this.saleItemPrice,
        saleItemDescription = this.saleItemDescription,
        purchaseId = this.purchaseId,
        purchaseDescription = this.purchaseDescription,
        supplierId = this.supplierId,
        supplierFullName = this.supplierFullName,
        purchaseItemPrice = this.purchaseItemPrice,
        purchaseItemDescription = this.purchaseItemDescription,
        createdAt = try { Instant.parse(this.createdAt).toEpochMilli() } catch (e: Exception) { 0L },
        updatedAt = try { Instant.parse(this.updatedAt).toEpochMilli() } catch (e: Exception) { 0L },
        deletedAt = this.deletedAt?.let { try { Instant.parse(it).toEpochMilli() } catch (e: Exception) { null } }
    )
}

// To map from Room Entity to Domain Model
fun StockMovementDetailsEntity.toDomain(): StockMovementDetails {
    return StockMovementDetails(
        transactionId = this.transactionId,
        stockMovementId = this.stockMovementId,
        transactionTypeId = this.transactionTypeId,
        transactionTypeName = this.transactionTypeName,
        userId = this.userId,
        username = this.username,
        transactionDate = this.transactionDate,
        transactionDescription = this.transactionDescription,
        movementTypeId = this.movementTypeId,
        movementTypeName = this.movementTypeName,
        movementTypeEffectOnStock = this.movementTypeEffectOnStock,
        movementTypeDescription = this.movementTypeDescription,
        stockMovementQuantity = this.stockMovementQuantity,
        stockMovementDescription = this.stockMovementDescription,
        seedBatchId = this.seedBatchId,
        seedBatchYear = this.seedBatchYear,
        seedBatchGrading = this.seedBatchGrading,
        seedBatchGermination = this.seedBatchGermination,
        seedBatchDescription = this.seedBatchDescription,
        riceVarietyId = this.riceVarietyId,
        riceVarietyName = this.riceVarietyName,
        riceVarietyDescription = this.riceVarietyDescription,
        riceVarietyImageUrl = this.riceVarietyImageUrl,
        generationId = this.generationId,
        generationName = this.generationName,
        generationDescription = this.generationDescription,
        seasonId = this.seasonId,
        seasonName = this.seasonName,
        seasonDescription = this.seasonDescription,
        saleId = this.saleId,
        customerId = this.customerId,
        customerFullName = this.customerFullName,
        saleDescription = this.saleDescription,
        saleItemPrice = this.saleItemPrice,
        saleItemDescription = this.saleItemDescription,
        purchaseId = this.purchaseId,
        purchaseDescription = this.purchaseDescription,
        supplierId = this.supplierId,
        supplierFullName = this.supplierFullName,
        purchaseItemPrice = this.purchaseItemPrice,
        purchaseItemDescription = this.purchaseItemDescription,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

fun RiceVarietyStockResponseDto.toDomain(): RiceVarietyStock {
    return RiceVarietyStock(
        riceVarietyId = this.riceVarietyId,
        riceVarietyName = this.riceVarietyName,
        riceVarietyImageUrl = this.riceVarietyImageUrl,
        totalQuantity = this.totalQuantity
    )
}

fun RiceVarietyStockPagedResponseDto.toDomain(): Page<RiceVarietyStock> {
    return Page(
        content = this.content.map { it.toDomain() },
        pageNumber = this.pageNumber,
        pageSize = this.pageSize,
        totalElements = this.totalElements,
        totalPages = this.totalPages,
        isLast = this.last
    )
}

fun TopSellingVarietyResponseDto.toDomain(): TopSellingVariety {
    return TopSellingVariety(
        riceVarietyId = this.riceVarietyId,
        riceVarietyName = this.riceVarietyName,
        riceVarietyImageUrl = this.riceVarietyImageUrl,
        totalSoldQuantity = this.totalSoldQuantity
    )
}

fun TopSellingVarietyPagedResponseDto.toDomain(): Page<TopSellingVariety> {
    return Page(
        content = this.content.map { it.toDomain() },
        pageNumber = this.pageNumber,
        pageSize = this.pageSize,
        totalElements = this.totalElements,
        totalPages = this.totalPages,
        isLast = this.last
    )
}
