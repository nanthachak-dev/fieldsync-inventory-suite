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
public class StockVarietySummaryResponseDTO {
    private Integer varietyId;
    private String varietyName;

    private BigDecimal stock;
    private BigDecimal r1Stock;
    private BigDecimal r2Stock;
    private BigDecimal r3Stock;

    private BigDecimal graded;
    private BigDecimal r1Graded;
    private BigDecimal r2Graded;
    private BigDecimal r3Graded;

    private BigDecimal ungraded;
    private BigDecimal r1Ungraded;
    private BigDecimal r2Ungraded;
    private BigDecimal r3Ungraded;

    private BigDecimal germinated;
    private BigDecimal r1Germinated;
    private BigDecimal r2Germinated;
    private BigDecimal r3Germinated;

    private BigDecimal ungerminated;
    private BigDecimal r1Ungerminated;
    private BigDecimal r2Ungerminated;
    private BigDecimal r3Ungerminated;
}
