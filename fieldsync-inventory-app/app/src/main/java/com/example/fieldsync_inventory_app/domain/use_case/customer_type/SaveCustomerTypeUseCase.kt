package com.example.fieldsync_inventory_app.domain.use_case.customer_type

import com.example.fieldsync_inventory_app.domain.repository.customer_type.CustomerTypeRepository
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import javax.inject.Inject

class SaveCustomerTypeUseCase @Inject constructor(
    private val repository: CustomerTypeRepository
) {
    suspend operator fun invoke(customerType: CustomerType) {
        repository.saveCustomerType(customerType)
    }
}