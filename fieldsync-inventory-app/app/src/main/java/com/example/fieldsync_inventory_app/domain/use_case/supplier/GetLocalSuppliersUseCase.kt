package com.example.fieldsync_inventory_app.domain.use_case.supplier

import com.example.fieldsync_inventory_app.domain.repository.supplier.SupplierRepository
import javax.inject.Inject

class GetLocalSuppliersUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    operator fun invoke() = repository.getLocalSuppliers()
}
