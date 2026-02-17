package com.example.fieldsync_inventory_app.domain.use_case.supplier

import com.example.fieldsync_inventory_app.domain.repository.supplier.SupplierRepository
import com.example.fieldsync_inventory_app.domain.model.Supplier
import javax.inject.Inject

class SaveSupplierUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    suspend operator fun invoke(supplier: Supplier) {
        repository.saveSupplier(supplier)
    }
}
