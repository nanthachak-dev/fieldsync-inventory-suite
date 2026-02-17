package com.example.fieldsync_inventory_app.domain.repository.supplier_type

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.SupplierTypeDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.SupplierTypeApi
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplierTypeRepositoryImpl @Inject constructor(
    private val api: SupplierTypeApi,
    private val dao: SupplierTypeDao
) : SupplierTypeRepository {

    override suspend fun syncSupplierTypes() {
        try {
            val networkSupplierTypes = api.getSupplierTypes()
            //Log.d("SupplierTypeRepository.syncSupplierTypes", "Fetched data of supplier types: $networkSupplierTypes")
            dao.insertAll(networkSupplierTypes.map { it.toEntity() })
            Log.d("SupplierTypeRepository", "Supplier types synced successfully")
        } catch (e: Exception) {
            Log.e("SupplierTypeRepository", "Error fetching from network", e)
            throw e
        }
    }

    override suspend fun saveSupplierType(supplierType: SupplierType) {
        if (supplierType.id == 0) {
            val response = api.createSupplierType(supplierType.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateSupplierType(supplierType.id, supplierType.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteSupplierType(id: Int) {
        api.deleteSupplierType(id)
        dao.deleteById(id)
    }

    override fun getLocalSupplierTypes(): Flow<List<SupplierType>> = flow {
        dao.getSupplierTypes().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}