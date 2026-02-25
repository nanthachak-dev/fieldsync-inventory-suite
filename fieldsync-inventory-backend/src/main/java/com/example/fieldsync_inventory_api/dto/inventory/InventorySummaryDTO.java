package com.example.fieldsync_inventory_api.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySummaryDTO {
    private BigDecimal totalStock;
    private BigDecimal totalGraded;
    private BigDecimal totalUngraded;
    private BigDecimal totalGerminated;
    private BigDecimal totalUngerminated;
}
