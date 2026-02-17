package com.example.fieldsync_inventory_app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.fieldsync_inventory_app.domain.repository.auth.AuthRepository
import com.example.fieldsync_inventory_app.domain.repository.auth.AuthRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.app_user.AppUserRepository
import com.example.fieldsync_inventory_app.domain.repository.app_user.AppUserRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.role.RoleRepository
import com.example.fieldsync_inventory_app.domain.repository.role.RoleRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.customer.CustomerRepository
import com.example.fieldsync_inventory_app.domain.repository.customer.CustomerRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.customer_type.CustomerTypeRepository
import com.example.fieldsync_inventory_app.domain.repository.customer_type.CustomerTypeRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.last_sync.LastSyncRepository
import com.example.fieldsync_inventory_app.domain.repository.last_sync.LastSyncRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepository
import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.season.SeasonRepository
import com.example.fieldsync_inventory_app.domain.repository.season.SeasonRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.seed_batch.SeedBatchRepository
import com.example.fieldsync_inventory_app.domain.repository.seed_batch.SeedBatchRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.seed_condition.SeedConditionRepository
import com.example.fieldsync_inventory_app.domain.repository.seed_condition.SeedConditionRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_type.StockMovementTypeRepository
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_type.StockMovementTypeRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.supplier.SupplierRepository
import com.example.fieldsync_inventory_app.domain.repository.supplier.SupplierRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.supplier_type.SupplierTypeRepository
import com.example.fieldsync_inventory_app.domain.repository.supplier_type.SupplierTypeRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepository
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.transaction_operation.TransactionOperationRepository
import com.example.fieldsync_inventory_app.domain.repository.transaction_operation.TransactionOperationRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.transaction_type.StockTransactionTypeRepository
import com.example.fieldsync_inventory_app.domain.repository.transaction_type.StockTransactionTypeRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary.StockTransactionSummaryRepository
import com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary.StockTransactionSummaryRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.inventory.InventoryRepository
import com.example.fieldsync_inventory_app.domain.repository.inventory.InventoryRepositoryImpl
import com.example.fieldsync_inventory_app.domain.repository.stock.StockRepository
import com.example.fieldsync_inventory_app.domain.repository.stock.StockRepositoryImpl

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRiceVarietyRepository(
        repositoryImpl: RiceVarietyRepositoryImpl
    ): RiceVarietyRepository

    @Binds
    @Singleton
    abstract fun bindSeasonRepository(
        repositoryImpl: SeasonRepositoryImpl
    ): SeasonRepository

    @Binds
    @Singleton
    abstract fun bindRiceGenerationRepository(
        repositoryImpl: RiceGenerationRepositoryImpl
    ): RiceGenerationRepository

    @Binds
    @Singleton
    abstract fun bindSeedConditionRepository(
        repositoryImpl: SeedConditionRepositoryImpl
    ): SeedConditionRepository

    @Binds
    @Singleton
    abstract fun bindStockMovementTypeRepository(
        repositoryImpl: StockMovementTypeRepositoryImpl
    ): StockMovementTypeRepository

    @Binds
    @Singleton
    abstract fun bindStockTransactionTypeRepository(
        repositoryImpl: StockTransactionTypeRepositoryImpl
    ): StockTransactionTypeRepository

    @Binds
    @Singleton
    abstract fun bindCustomerTypeRepository(
        repositoryImpl: CustomerTypeRepositoryImpl
    ): CustomerTypeRepository

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        repositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindSeedBatchRepository(
        repositoryImpl: SeedBatchRepositoryImpl
    ): SeedBatchRepository

    @Binds
    @Singleton
    abstract fun bindTransactionOperationRepository(
        repositoryImpl: TransactionOperationRepositoryImpl
    ): TransactionOperationRepository

    @Binds
    @Singleton
    abstract fun bindStockMovementDetailsRepository(
        repositoryImpl: StockMovementDetailsRepositoryImpl
    ): StockMovementDetailsRepository

    @Binds
    @Singleton
    abstract fun bindLastSyncRepository(
        repositoryImpl: LastSyncRepositoryImpl
    ): LastSyncRepository

    @Binds
    @Singleton
    abstract fun bindSupplierTypeRepository(
        repositoryImpl: SupplierTypeRepositoryImpl
    ): SupplierTypeRepository

    @Binds
    @Singleton
    abstract fun bindSupplierRepository(
        repositoryImpl: SupplierRepositoryImpl
    ): SupplierRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAppUserRepository(
        repositoryImpl: AppUserRepositoryImpl
    ): AppUserRepository

    @Binds
    @Singleton
    abstract fun bindRoleRepository(
        repositoryImpl: RoleRepositoryImpl
    ): RoleRepository

    @Binds
    @Singleton
    abstract fun bindStockTransactionSummaryRepository(
        repositoryImpl: StockTransactionSummaryRepositoryImpl
    ): StockTransactionSummaryRepository

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        repositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        repositoryImpl: StockRepositoryImpl
    ): StockRepository
}

