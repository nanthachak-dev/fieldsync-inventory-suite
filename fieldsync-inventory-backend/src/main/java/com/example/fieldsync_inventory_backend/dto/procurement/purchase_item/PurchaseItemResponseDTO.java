package com.example.fieldsync_inventory_backend.dto.procurement.purchase_item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase.PurchaseCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement.StockMovementCompactDTO;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemResponseDTO {
    private StockMovementCompactDTO stockMovement;
    private PurchaseCompactDTO purchase;
    private BigDecimal price;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
