package com.example.fieldsync_inventory_app.domain.use_case.customer

import com.example.fieldsync_inventory_app.domain.repository.customer.CustomerRepository
import javax.inject.Inject

class SyncCustomersUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend fun sync(){
        repository.syncCustomers()
    }
}