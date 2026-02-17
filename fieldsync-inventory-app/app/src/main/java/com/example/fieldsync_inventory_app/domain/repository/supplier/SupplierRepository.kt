package com.example.fieldsync_inventory_app.domain.repository.supplier

import com.example.fieldsync_inventory_app.domain.model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    suspend fun syncSuppliers()
    suspend fun saveSupplier(supplier: Supplier)
    suspend fun deleteSupplier(id: Int)
    fun getLocalSuppliers(): Flow<List<Supplier>>
}
