package com.example.fieldsync_inventory_api.dto.procurement.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.procurement.supplier.SupplierCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionCompactDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseCompactDTO {
    private StockTransactionCompactDTO stockTransaction;
    private SupplierCompactDTO supplier;
    private String description;
}
