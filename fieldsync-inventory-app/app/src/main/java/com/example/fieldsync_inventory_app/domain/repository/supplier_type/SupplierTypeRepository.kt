package com.example.fieldsync_inventory_app.domain.repository.supplier_type

import com.example.fieldsync_inventory_app.domain.model.SupplierType
import kotlinx.coroutines.flow.Flow

interface SupplierTypeRepository {
    suspend fun syncSupplierTypes()
    suspend fun saveSupplierType(supplierType: SupplierType)
    suspend fun deleteSupplierType(id: Int)
    fun getLocalSupplierTypes(): Flow<List<SupplierType>>
}