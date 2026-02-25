package com.example.fieldsync_inventory_api.dto.procurement.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequestDTO {
    private Long stockTransactionId;
    private Integer supplierId;
    private String description;
}
