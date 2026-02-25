package com.example.fieldsync_inventory_api.dto.procurement.purchase_item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemRequestDTO {
    private Long stockMovementId;
    private Long purchaseId;
    private BigDecimal price;
    private String description;
}
