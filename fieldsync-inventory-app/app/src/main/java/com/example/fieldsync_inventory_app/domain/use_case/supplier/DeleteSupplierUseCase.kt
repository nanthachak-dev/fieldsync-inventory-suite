package com.example.fieldsync_inventory_app.domain.use_case.supplier

import com.example.fieldsync_inventory_app.domain.repository.supplier.SupplierRepository
import javax.inject.Inject

class DeleteSupplierUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteSupplier(id)
    }
}
