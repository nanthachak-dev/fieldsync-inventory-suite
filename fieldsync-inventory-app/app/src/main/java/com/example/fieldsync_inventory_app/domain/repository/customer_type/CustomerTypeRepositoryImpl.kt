package com.example.fieldsync_inventory_app.domain.repository.customer_type

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.CustomerTypeDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.CustomerTypeApi
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerTypeRepositoryImpl @Inject constructor(
    private val api: CustomerTypeApi,
    private val dao: CustomerTypeDao
) : CustomerTypeRepository {

    override suspend fun syncCustomerTypes() {
        try {
            val networkCustomerTypes = api.getCustomerTypes()
            //Log.d("StockMovementTypeRepo", "Fetched from network: ${networkCustomerTypes.map { it.name }}")
            dao.insertAll(networkCustomerTypes.map { it.toEntity() })
            Log.d("CustomerTypeRepository", "Customer types synced successfully")
        } catch (e: Exception) {
            Log.e("CustomerTypeRepository", "Error fetching from network", e)
            throw e
        }
    }

    override suspend fun saveCustomerType(customerType: CustomerType) {
        if (customerType.id == 0) {
            val response = api.createCustomerType(customerType.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateCustomerType(customerType.id, customerType.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteCustomerType(id: Int) {
        api.deleteCustomerType(id)
        dao.deleteById(id)
    }

    override fun getLocalCustomerTypes(): Flow<List<CustomerType>> = flow {
        dao.getCustomerTypes().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}