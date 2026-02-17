package com.example.fieldsync_inventory_app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
import com.example.fieldsync_inventory_app.data.local.database.RcrcSeedLocalDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataModule {
    companion object {
        // Room
        @Provides
        @Singleton
        fun provideRcrcSeedLocalDatabase(
            @ApplicationContext context: Context
        ): RcrcSeedLocalDatabase {
            return Room.databaseBuilder(
                context,
                RcrcSeedLocalDatabase::class.java,
                "rcrc_seed"
            )
                .fallbackToDestructiveMigration(true) // For development - removes all data on schema changes
                .build()
        }

        @Provides
        fun provideRiceVarietyDao(database: RcrcSeedLocalDatabase): RiceVarietyDao {
            return database.riceVarietyDao()
        }

        @Provides
        fun provideSeasonDao(database: RcrcSeedLocalDatabase): SeasonDao {
            return database.seasonDao()
        }

        @Provides
        fun provideRiceGenerationDao(database: RcrcSeedLocalDatabase): RiceGenerationDao {
            return database.riceGenerationDao()
        }

        @Provides
        fun provideSeedConditionDao(database: RcrcSeedLocalDatabase): SeedConditionDao {
            return database.seedConditionDao()
        }

        @Provides
        fun provideStockMovementTypeDao(database: RcrcSeedLocalDatabase): StockMovementTypeDao {
            return database.stockMovementTypeDao()
        }

        @Provides
        fun provideStockTransactionTypeDao(database: RcrcSeedLocalDatabase): StockTransactionTypeDao {
            return database.stockTransactionTypeDao()
        }

        @Provides
        fun provideCustomerTypeDao(database: RcrcSeedLocalDatabase): CustomerTypeDao {
            return database.customerTypeDao()
        }

        @Provides
        fun provideCustomerDao(database: RcrcSeedLocalDatabase): CustomerDao {
            return database.customerDao()
        }

        @Provides
        fun provideSeedBatchDao(database: RcrcSeedLocalDatabase): SeedBatchDao {
            return database.seedBatchDao()
        }

        @Provides
        fun provideStockMovementDetailsDao(database: RcrcSeedLocalDatabase): StockMovementDetailsDao {
            return database.stockMovementDetailsDao()
        }

        @Provides
        fun provideLastSyncDao(database: RcrcSeedLocalDatabase): LastSyncDao {
            return database.lastSyncDao()
        }

        @Provides
        fun provideSupplierTypeDao(database: RcrcSeedLocalDatabase): SupplierTypeDao {
            return database.supplierTypeDao()
        }

        @Provides
        fun provideSupplierDao(database: RcrcSeedLocalDatabase): SupplierDao {
            return database.supplierDao()
        }
    }
}
