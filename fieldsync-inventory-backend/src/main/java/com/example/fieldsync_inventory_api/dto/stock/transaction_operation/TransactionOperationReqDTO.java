package com.example.fieldsync_inventory_api.dto.stock.transaction_operation;

import lombok.*;
import com.example.fieldsync_inventory_api.dto.sales.sale.SaleRequestDTO;

import java.time.Instant;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionOperationReqDTO {
    private Integer transactionTypeId;
    private Integer performedById;
    private Instant transactionDate;
    private String description;
    private SaleRequestDTO saleRequestDTO; // Could be null, for non-sale movement
    ArrayList<StockMovementTOReqDTO> stockMovements; // one-two-man field like, because a transaction may contain many movement records
}
