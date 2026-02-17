package com.example.fieldsync_inventory_backend.dto.stock.transaction_operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Sale Item Transaction Operation Response DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemTOResDTO {
    private BigDecimal price;
    private String description;
}