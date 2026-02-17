package com.example.fieldsync_inventory_backend.dto.sales.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleRequestDTO {
    private Long stockTransactionId; // Must specify id in request
    private Integer customerId;
    private String description;
}