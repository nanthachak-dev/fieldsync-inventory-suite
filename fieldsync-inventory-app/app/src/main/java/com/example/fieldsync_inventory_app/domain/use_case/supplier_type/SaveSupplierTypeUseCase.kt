package com.example.fieldsync_inventory_app.domain.use_case.supplier_type

import com.example.fieldsync_inventory_app.domain.repository.supplier_type.SupplierTypeRepository
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import javax.inject.Inject

class SaveSupplierTypeUseCase @Inject constructor(
    private val repository: SupplierTypeRepository
) {
    suspend operator fun invoke(supplierType: SupplierType) {
        repository.saveSupplierType(supplierType)
    }
}