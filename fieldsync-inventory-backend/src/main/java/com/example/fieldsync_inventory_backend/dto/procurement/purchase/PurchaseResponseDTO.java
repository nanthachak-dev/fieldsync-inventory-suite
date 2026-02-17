package com.example.fieldsync_inventory_backend.dto.procurement.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier.SupplierCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction.StockTransactionCompactDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponseDTO {
    private StockTransactionCompactDTO stockTransaction;
    private SupplierCompactDTO supplier;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
