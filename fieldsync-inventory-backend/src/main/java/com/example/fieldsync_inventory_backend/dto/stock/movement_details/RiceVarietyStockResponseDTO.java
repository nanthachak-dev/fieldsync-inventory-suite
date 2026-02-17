package com.example.fieldsync_inventory_backend.dto.stock.movement_details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiceVarietyStockResponseDTO {
    private Integer riceVarietyId;
    private String riceVarietyName;
    private String riceVarietyImageUrl;
    private BigDecimal totalQuantity;
}
