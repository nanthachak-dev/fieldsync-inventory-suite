package com.example.fieldsync_inventory_app.domain.use_case.customer_type

import com.example.fieldsync_inventory_app.domain.repository.customer_type.CustomerTypeRepository
import javax.inject.Inject

class GetLocalCustomerTypesUseCase @Inject constructor(
    private val repository: CustomerTypeRepository
) {
    operator fun invoke() = repository.getLocalCustomerTypes()
}