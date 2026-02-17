package com.example.fieldsync_inventory_app.domain.use_case.customer

import com.example.fieldsync_inventory_app.domain.repository.customer.CustomerRepository
import com.example.fieldsync_inventory_app.domain.model.Customer
import javax.inject.Inject

class SaveCustomerUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(customer: Customer) {
        repository.saveCustomer(customer)
    }
}