package com.example.fieldsync_inventory_app.domain.repository.customer

import com.example.fieldsync_inventory_app.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun syncCustomers()
    suspend fun saveCustomer(customer: Customer)
    suspend fun deleteCustomer(id: Int)
    fun getLocalCustomers(): Flow<List<Customer>>
}