package com.example.fieldsync_inventory_api.dto.stock.movement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.seed_batch.SeedBatchCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_type.StockMovementTypeCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionCompactDTO;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementResponseDTO {
    private Long id;
    private StockMovementTypeCompactDTO movementType;
    private SeedBatchCompactDTO seedBatch; // Changed to match the SeedBatch entity's Long ID
    private StockTransactionCompactDTO stockTransaction;
    private BigDecimal quantity;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
