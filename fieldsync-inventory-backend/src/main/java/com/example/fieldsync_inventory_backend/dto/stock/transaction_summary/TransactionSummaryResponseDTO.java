package com.example.fieldsync_inventory_backend.dto.stock.transaction_summary;

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
public class TransactionSummaryResponseDTO {
    private Long transactionId;
    private Instant transactionDate;
    private Integer transactionTypeId;
    private String transactionTypeName;
    private String transactionDescription;
    private String username;
    private String mainMovementType;
    private Long itemCount;
    private BigDecimal totalQuantity;
    private BigDecimal totalSalePrice;
    private BigDecimal totalPurchasePrice;
    private String customerName;
    private String supplierName;
    private Long fromBatchId;
    private Long toBatchId;
}
