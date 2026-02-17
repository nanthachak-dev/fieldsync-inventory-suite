package com.example.fieldsync_inventory_app.domain.repository.customer_type

import com.example.fieldsync_inventory_app.domain.model.CustomerType
import kotlinx.coroutines.flow.Flow

interface CustomerTypeRepository {
    suspend fun syncCustomerTypes()
    suspend fun saveCustomerType(customerType: CustomerType)
    suspend fun deleteCustomerType(id: Int)
    fun getLocalCustomerTypes(): Flow<List<CustomerType>>
}