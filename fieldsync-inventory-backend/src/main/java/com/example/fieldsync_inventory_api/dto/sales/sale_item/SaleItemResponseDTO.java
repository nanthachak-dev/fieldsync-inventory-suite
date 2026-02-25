package com.example.fieldsync_inventory_api.dto.sales.sale_item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.sales.sale.SaleCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement.StockMovementCompactDTO;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemResponseDTO {
    private StockMovementCompactDTO stockMovement; // Shared PK
    private SaleCompactDTO sale; // Many-to-one
    private BigDecimal price;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}