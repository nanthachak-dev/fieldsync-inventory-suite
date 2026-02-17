package com.example.fieldsync_inventory_app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fieldsync_inventory_app.data.local.dao.CustomerDao
import com.example.fieldsync_inventory_app.data.local.dao.CustomerTypeDao
import com.example.fieldsync_inventory_app.data.local.dao.LastSyncDao
import com.example.fieldsync_inventory_app.data.local.dao.RiceGenerationDao
import com.example.fieldsync_inventory_app.data.local.dao.RiceVarietyDao
import com.example.fieldsync_inventory_app.data.local.dao.SeasonDao
import com.example.fieldsync_inventory_app.data.local.dao.SeedBatchDao
import com.example.fieldsync_inventory_app.data.local.dao.SeedConditionDao
import com.example.fieldsync_inventory_app.data.local.dao.StockMovementTypeDao
import com.example.fieldsync_inventory_app.data.local.dao.StockMovementDetailsDao
import com.example.fieldsync_inventory_app.data.local.dao.StockTransactionTypeDao
import com.example.fieldsync_inventory_app.data.local.dao.SupplierDao
import com.example.fieldsync_inventory_app.data.local.dao.SupplierTypeDao
import com.example.fieldsync_inventory_app.data.local.entity.CustomerEntity
import com.example.fieldsync_inventory_app.data.local.entity.CustomerTypeEntity
import com.example.fieldsync_inventory_app.data.local.entity.LastSyncEntity
import com.example.fieldsync_inventory_app.data.local.entity.RiceGenerationEntity
import com.example.fieldsync_inventory_app.data.local.entity.RiceVarietyEntity
import com.example.fieldsync_inventory_app.data.local.entity.SeasonEntity
import com.example.fieldsync_inventory_app.data.local.entity.SeedBatchEntity
import com.example.fieldsync_inventory_app.data.local.entity.SeedConditionEntity
import com.example.fieldsync_inventory_app.data.local.entity.StockMovementTypeEntity
import com.example.fieldsync_inventory_app.data.local.entity.StockMovementDetailsEntity
import com.example.fieldsync_inventory_app.data.local.entity.StockTransactionTypeEntity
import com.example.fieldsync_inventory_app.data.local.entity.SupplierEntity
import com.example.fieldsync_inventory_app.data.local.entity.SupplierTypeEntity

@Database(
    entities = [
        RiceVarietyEntity::class, SeasonEntity::class, RiceGenerationEntity::class,
        SeedConditionEntity::class, StockMovementTypeEntity::class, StockTransactionTypeEntity::class,
        SeedBatchEntity::class, CustomerTypeEntity::class, CustomerEntity::class,
        StockMovementDetailsEntity::class, LastSyncEntity::class, SupplierTypeEntity::class, SupplierEntity::class
    ],
    version = 7, // Refactor Stock Transaction Details Entity
    exportSchema = true
)
abstract class RcrcSeedLocalDatabase : RoomDatabase() {
    abstract fun riceVarietyDao(): RiceVarietyDao
    abstract fun seasonDao(): SeasonDao
    abstract fun riceGenerationDao(): RiceGenerationDao
    abstract fun seedConditionDao(): SeedConditionDao
    abstract fun stockMovementTypeDao(): StockMovementTypeDao
    abstract fun stockTransactionTypeDao(): StockTransactionTypeDao
    abstract fun customerTypeDao(): CustomerTypeDao
    abstract fun customerDao(): CustomerDao
    abstract fun seedBatchDao(): SeedBatchDao
    abstract fun stockMovementDetailsDao(): StockMovementDetailsDao
    abstract fun lastSyncDao(): LastSyncDao
    abstract fun supplierTypeDao(): SupplierTypeDao
    abstract fun supplierDao(): SupplierDao
}
