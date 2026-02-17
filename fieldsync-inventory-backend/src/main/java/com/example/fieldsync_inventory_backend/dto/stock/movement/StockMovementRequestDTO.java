package com.example.fieldsync_inventory_backend.dto.stock.movement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementRequestDTO {
    private Integer movementTypeId;
    private Long seedBatchId;
    private Long stockTransactionId;
    private BigDecimal quantity;
    private String description;
}
