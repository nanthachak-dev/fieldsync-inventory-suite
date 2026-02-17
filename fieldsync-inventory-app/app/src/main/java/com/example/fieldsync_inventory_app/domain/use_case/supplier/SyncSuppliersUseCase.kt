package com.example.fieldsync_inventory_app.domain.use_case.supplier

import com.example.fieldsync_inventory_app.domain.repository.supplier.SupplierRepository
import javax.inject.Inject

class SyncSuppliersUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    suspend fun sync(){
        repository.syncSuppliers()
    }
}
