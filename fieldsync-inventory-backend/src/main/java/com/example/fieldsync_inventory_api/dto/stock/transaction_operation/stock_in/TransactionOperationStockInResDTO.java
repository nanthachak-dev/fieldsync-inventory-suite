package com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.enums.OperationTask;
import com.example.fieldsync_inventory_api.enums.OperationType;

import java.time.Instant;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionOperationStockInResDTO {
    private Long transactionId;
    private OperationType type; // transaction types
    private OperationTask task; // movement types which are the same each
    private String supplier; // Purchase's supplier
    ArrayList<TOStockMovementStockInResDTO> stockMovements;
    private Instant transactionDate;
    private String description;
    private String performedBy; // user
}
