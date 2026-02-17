package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "stock_movement_details", primaryKeys = ["transactionId", "stockMovementId"])
data class StockMovementDetailsEntity(
    val transactionId: Long,
    val stockMovementId: Long,
    val transactionTypeId: Int,
    val transactionTypeName: String,
    val userId: Int,
    val username: String,
    @ColumnInfo(name = "transaction_date")
    val transactionDate: Long,
    @ColumnInfo(name = "transaction_description")
    val transactionDescription: String?,
    val movementTypeId: Int,
    val movementTypeName: String,
    @ColumnInfo(name = "movement_type_effect_on_stock")
    val movementTypeEffectOnStock: String,
    @ColumnInfo(name = "movement_type_description")
    val movementTypeDescription: String?,
    @ColumnInfo(name = "stock_movement_quantity")
    val stockMovementQuantity: Double,
    @ColumnInfo(name = "stock_movement_description")
    val stockMovementDescription: String?,
    val seedBatchId: Long,
    val seedBatchYear: Int,
    val seedBatchGrading: Boolean,
    val seedBatchGermination: Boolean,
    @ColumnInfo(name = "seed_batch_description")
    val seedBatchDescription: String?,
    val riceVarietyId: Int,
    val riceVarietyName: String,
    @ColumnInfo(name = "rice_variety_description")
    val riceVarietyDescription: String?,
    @ColumnInfo(name = "rice_variety_image_url")
    val riceVarietyImageUrl: String?,
    val generationId: Int,
    val generationName: String,
    @ColumnInfo(name = "generation_description")
    val generationDescription: String?,
    val seasonId: Int,
    val seasonName: String,
    @ColumnInfo(name = "season_description")
    val seasonDescription: String?,
    val saleId: Long?,
    val customerId: Int?,
    @ColumnInfo(name = "customer_full_name")
    val customerFullName: String?,
    @ColumnInfo(name = "sale_description")
    val saleDescription: String?,
    @ColumnInfo(name = "sale_item_price")
    val saleItemPrice: Double?,
    @ColumnInfo(name = "sale_item_description")
    val saleItemDescription: String?,
    val purchaseId: Long?,
    @ColumnInfo(name = "purchase_description")
    val purchaseDescription: String?,
    val supplierId: Int?,
    @ColumnInfo(name = "supplier_full_name")
    val supplierFullName: String?,
    @ColumnInfo(name = "purchase_item_price")
    val purchaseItemPrice: Double?,
    @ColumnInfo(name = "purchase_item_description")
    val purchaseItemDescription: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?
)