package com.example.fieldsync_inventory_api.dto.stock.transaction_operation;

import lombok.*;
import com.example.fieldsync_inventory_api.dto.inventory.seed_batch.SeedBatchRequestDTO;
import com.example.fieldsync_inventory_api.dto.sales.sale_item.SaleItemRequestDTO;

import java.math.BigDecimal;

// Stock Movement Transaction Operation Request DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementTOReqDTO {
    private Long id; // Because it one to many on Transaction so the id is required for update operation
    private Integer movementTypeId;
    private SeedBatchRequestDTO seedBatch; // For create new if not found
    private SaleItemRequestDTO saleItem; // stockMovementId and saleId will be ignored

    private BigDecimal quantity;
    private String description;
}
