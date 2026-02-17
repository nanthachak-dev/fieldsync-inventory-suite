package com.example.fieldsync_inventory_app.domain.use_case.customer

import com.example.fieldsync_inventory_app.domain.repository.customer.CustomerRepository
import javax.inject.Inject

class DeleteCustomerUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteCustomer(id)
    }
}