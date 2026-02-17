package com.example.fieldsync_inventory_app.domain.model

data class StockMovementDetails(
    val transactionId: Long,
    val stockMovementId: Long,
    val transactionTypeId: Int,
    val transactionTypeName: String,
    val userId: Int,
    val username: String,
    val transactionDate: Long, // Converted from String (ISO 8601) to Long (Epoch Milliseconds)
    val transactionDescription: String?,
    val movementTypeId: Int,
    val movementTypeName: String,
    val movementTypeEffectOnStock: String,
    val movementTypeDescription: String?,
    val stockMovementQuantity: Double,
    val stockMovementDescription: String?,
    val seedBatchId: Long,
    val seedBatchYear: Int,
    val seedBatchGrading: Boolean,
    val seedBatchGermination: Boolean,
    val seedBatchDescription: String?,
    val riceVarietyId: Int,
    val riceVarietyName: String,
    val riceVarietyDescription: String?,
    val riceVarietyImageUrl: String?,
    val generationId: Int,
    val generationName: String,
    val generationDescription: String?,
    val seasonId: Int,
    val seasonName: String,
    val seasonDescription: String?,
    val saleId: Long?, // Nullable since it might not always be a Sale transaction
    val customerId: Int?, // Nullable
    val customerFullName: String?, // Nullable
    val saleDescription: String?, // Nullable
    val saleItemPrice: Double?, // Nullable
    val saleItemDescription: String?, // Nullable
    val purchaseId: Long?, // Nullable
    val purchaseDescription: String?, // Nullable
    val supplierId: Int?, // Nullable
    val supplierFullName: String?, // Nullable
    val purchaseItemPrice: Double?, // Nullable
    val purchaseItemDescription: String?, // Nullable
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)