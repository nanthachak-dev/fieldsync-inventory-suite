package com.example.fieldsync_inventory_app.domain.use_case.customer_type

import com.example.fieldsync_inventory_app.domain.repository.customer_type.CustomerTypeRepository
import javax.inject.Inject

class SyncCustomerTypesUseCase @Inject constructor(
    private val repository: CustomerTypeRepository
) {
    suspend fun sync(){
        repository.syncCustomerTypes()
    }
}