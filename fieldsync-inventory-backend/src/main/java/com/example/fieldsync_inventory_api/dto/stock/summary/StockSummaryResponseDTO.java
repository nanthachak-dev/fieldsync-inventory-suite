package com.example.fieldsync_inventory_api.dto.stock.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockSummaryResponseDTO {
    private BigDecimal totalStock;
    private BigDecimal totalR1Stock;
    private BigDecimal totalR2Stock;
    private BigDecimal totalR3Stock;

    private BigDecimal totalGraded;
    private BigDecimal totalR1Graded;
    private BigDecimal totalR2Graded;
    private BigDecimal totalR3Graded;

    private BigDecimal totalUngraded;
    private BigDecimal totalR1Ungraded;
    private BigDecimal totalR2Ungraded;
    private BigDecimal totalR3Ungraded;

    private BigDecimal totalGerminated;
    private BigDecimal totalR1Germinated;
    private BigDecimal totalR2Germinated;
    private BigDecimal totalR3Germinated;

    private BigDecimal totalUngerminated;
    private BigDecimal totalR1Ungerminated;
    private BigDecimal totalR2Ungerminated;
    private BigDecimal totalR3Ungerminated;
}
