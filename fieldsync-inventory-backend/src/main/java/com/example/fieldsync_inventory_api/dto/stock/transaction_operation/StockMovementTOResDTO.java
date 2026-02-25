package com.example.fieldsync_inventory_api.dto.stock.transaction_operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.seed_batch.SeedBatchCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_type.StockMovementTypeCompactDTO;

import java.math.BigDecimal;

// Stock Movement For Transaction Operation Response DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementTOResDTO {
    private Long id;
    private StockMovementTypeCompactDTO movementType;
    private SeedBatchCompactDTO seedBatch; // For create new if not found
    private SaleItemTOResDTO saleItem; // stockMovementId and saleId will be ignored

    private BigDecimal quantity;
    private String description;
}
