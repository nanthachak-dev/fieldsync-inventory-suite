package com.example.fieldsync_inventory_backend.dto.stock.transaction_operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.customer.CustomerCompactDTO;

import java.math.BigDecimal;

// Transaction Operation Sale Response Data Transfer Object
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TOSaleResDTO {
    // id is the same as transaction's
    private CustomerCompactDTO customer; // id and full name
    private BigDecimal sumSale;
}
