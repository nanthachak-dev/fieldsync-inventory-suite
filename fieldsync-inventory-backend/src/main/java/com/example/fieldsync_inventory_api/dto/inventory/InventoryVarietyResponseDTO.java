package com.example.fieldsync_inventory_api.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryVarietyResponseDTO {
    private Integer varietyId;
    private String varietyName;
    private BigDecimal stock;
    private BigDecimal graded;
    private BigDecimal ungraded;
    private BigDecimal germinated;
    private BigDecimal ungerminated;
}
