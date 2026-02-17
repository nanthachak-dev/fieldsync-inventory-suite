package com.example.fieldsync_inventory_backend.dto.stock.transaction_summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCountResponseDTO {
    private long totalTransactions;
    private Instant startDate;
    private Instant endDate;
}
