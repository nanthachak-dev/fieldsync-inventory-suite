package com.example.fieldsync_inventory_backend.dto.sales.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.customer.CustomerCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction.StockTransactionCompactDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponseDTO {
    private StockTransactionCompactDTO stockTransaction;
    private CustomerCompactDTO customer;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}