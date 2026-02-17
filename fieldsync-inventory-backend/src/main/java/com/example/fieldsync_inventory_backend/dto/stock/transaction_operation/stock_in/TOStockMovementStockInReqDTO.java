package com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.stock_in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase_item.PurchaseItemRequestDTO;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TOStockMovementStockInReqDTO {
    private Long id; // The id is required for update operation, for create, is null
    private Integer movementTypeId; // Movement type
    private SeedBatchRequestDTO seedBatch; // For create new if not found
    private PurchaseItemRequestDTO purchaseItem; // stockMovementId and purchaseId will be ignored on POST method

    private BigDecimal quantity;
    private String description;
}
