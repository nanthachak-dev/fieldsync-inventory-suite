package com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.procurement.purchase.PurchaseRequestDTO;
import com.example.fieldsync_inventory_api.enums.OperationTask;

import java.time.Instant;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionOperationStockInReqDTO {
    private Integer transactionTypeId; // Transaction type
    private OperationTask task; // define movement type for all new movement
    private Instant transactionDate;
    private Integer performedBy; // user_id
    private PurchaseRequestDTO purchase; // Could be null, for non-sale movement
    ArrayList<TOStockMovementStockInReqDTO> stockMovements; // one-two-man field like, because a transaction may contain many movement records
    private String description;
}
