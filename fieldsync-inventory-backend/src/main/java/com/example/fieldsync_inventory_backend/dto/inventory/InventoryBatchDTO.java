package com.example.fieldsync_inventory_backend.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryBatchDTO {
    private Long batchId;
    private Integer varietyId;
    private String varietyName;
    private Integer year;
    private Integer seasonId;
    private String seasonName;
    private String seasonDescription;
    private Integer generationId;
    private String generationName;
    private Boolean grading;
    private Boolean germination;
    private BigDecimal stock;
}
