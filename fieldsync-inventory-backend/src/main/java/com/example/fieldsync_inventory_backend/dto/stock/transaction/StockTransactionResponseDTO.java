package com.example.fieldsync_inventory_backend.dto.stock.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.user.user.AppUserCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_type.StockTransactionTypeCompactDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransactionResponseDTO {
    private Long id;
    private StockTransactionTypeCompactDTO transactionType;
    private AppUserCompactDTO performedBy;
    private Instant transactionDate;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
