package com.example.fieldsync_inventory_api.dto.stock.transaction_summary;

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
public class TotalSoldOutResponseDTO {
    private BigDecimal totalSoldOut;
    private Instant startDate;
    private Instant endDate;
}
