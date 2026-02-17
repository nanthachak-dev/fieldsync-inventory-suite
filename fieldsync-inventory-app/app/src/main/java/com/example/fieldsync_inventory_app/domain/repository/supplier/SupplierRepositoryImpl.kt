package com.example.fieldsync_inventory_app.domain.repository.supplier

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.SupplierDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.SupplierApi
import com.example.fieldsync_inventory_app.domain.model.Supplier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplierRepositoryImpl @Inject constructor(
    private val api: SupplierApi,
    private val dao: SupplierDao
) : SupplierRepository {

    override suspend fun syncSuppliers() {
        try {
            val networkSuppliers = api.getSuppliers()
            //Log.d("SupplierRepository", "Fetched ${networkSuppliers.size} records of supplier: $networkSuppliers")
            dao.insertAll(networkSuppliers.map { it.toEntity() })
            Log.d("SupplierRepository", "Supplier synced successfully")
        } catch (e: Exception) {
            Log.e("SupplierRepository", "Error fetching from network", e)
            throw e
        }
    }

    override suspend fun saveSupplier(supplier: Supplier) {
        if (supplier.id == 0) {
            val response = api.createSupplier(supplier.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateSupplier(supplier.id, supplier.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteSupplier(id: Int) {
        api.deleteSupplier(id)
        dao.deleteById(id)
    }

    override fun getLocalSuppliers(): Flow<List<Supplier>> = flow {
        dao.getSuppliers().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}
