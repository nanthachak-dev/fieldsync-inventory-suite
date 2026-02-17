package com.example.fieldsync_inventory_backend.dto.sales.sale_item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemRequestDTO {
    private Long stockMovementId; // Shared PK
    private Long saleId; // Many-to-one
    private BigDecimal price;
    private String description;
}