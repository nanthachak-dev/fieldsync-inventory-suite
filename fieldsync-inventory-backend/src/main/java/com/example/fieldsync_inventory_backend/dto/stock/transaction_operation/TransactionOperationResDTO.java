package com.example.fieldsync_inventory_backend.dto.stock.transaction_operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.user.user.AppUserCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_type.StockTransactionTypeCompactDTO;

import java.time.Instant;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionOperationResDTO {
    private Long transactionId;
    private StockTransactionTypeCompactDTO transactionType;
    private AppUserCompactDTO performedBy;
    ArrayList<StockMovementTOResDTO> stockMovements;
    private TOSaleResDTO sale;

    private Instant transactionDate;
    private String description;
}
