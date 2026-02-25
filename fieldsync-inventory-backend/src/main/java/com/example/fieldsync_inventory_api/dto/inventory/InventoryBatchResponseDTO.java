package com.example.fieldsync_inventory_api.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryBatchResponseDTO {
    private BigDecimal totalStock;
    private BigDecimal totalGraded;
    private BigDecimal totalUngraded;
    private BigDecimal totalGerminated;
    private BigDecimal totalUngerminated;
    private List<InventoryBatchDTO> batches;
}
