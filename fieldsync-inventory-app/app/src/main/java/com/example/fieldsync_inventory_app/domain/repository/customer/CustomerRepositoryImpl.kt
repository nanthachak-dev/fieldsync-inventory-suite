package com.example.fieldsync_inventory_app.domain.repository.customer

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.CustomerDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.CustomerApi
import com.example.fieldsync_inventory_app.domain.model.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val api: CustomerApi,
    private val dao: CustomerDao
) : CustomerRepository {

    override suspend fun syncCustomers() {
        try {
            val networkCustomers = api.getCustomers()
            //Log.d("StockMovementTypeRepo", "Fetched from network: $networkCustomers")
            dao.insertAll(networkCustomers.map { it.toEntity() })
            Log.d("CustomerRepository", "Customer synced successfully")
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error fetching from network", e)
            throw e
        }
    }

    override suspend fun saveCustomer(customer: Customer) {
        if (customer.id == 0) {
            val response = api.createCustomer(customer.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateCustomer(customer.id, customer.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteCustomer(id: Int) {
        api.deleteCustomer(id)
        dao.deleteById(id)
    }

    override fun getLocalCustomers(): Flow<List<Customer>> = flow {
        dao.getCustomers().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}