package com.example.fieldsync_inventory_app.domain.use_case.supplier_type

import com.example.fieldsync_inventory_app.domain.repository.supplier_type.SupplierTypeRepository
import javax.inject.Inject

class DeleteSupplierTypeUseCase @Inject constructor(
    private val repository: SupplierTypeRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteSupplierType(id)
    }
}