package com.example.fieldsync_inventory_backend.dto.stock.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransactionRequestDTO {
    private Integer transactionTypeId;
    private Integer performedById;
    private Instant transactionDate;
    private String description;
}
