package com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Transaction Operation Stock Movement Stock Input Response DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TOStockMovementStockInResDTO {
    private Long id;
    private String movementType;
    private Long seedBatchId; // For create new if not found
    private BigDecimal purchaseItem; // stockMovementId and saleId will be ignored

    private BigDecimal quantity;
    private String description;
}
