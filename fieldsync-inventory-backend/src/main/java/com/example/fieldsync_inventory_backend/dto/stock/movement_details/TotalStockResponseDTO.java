package com.example.fieldsync_inventory_backend.dto.stock.movement_details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalStockResponseDTO {
    private BigDecimal totalStock;
    private Instant asOfDate;
}
