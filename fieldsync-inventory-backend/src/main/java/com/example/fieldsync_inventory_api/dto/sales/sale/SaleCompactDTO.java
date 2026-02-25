package com.example.fieldsync_inventory_api.dto.sales.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.sales.customer.CustomerCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionCompactDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleCompactDTO {
    private StockTransactionCompactDTO stockTransaction;
    private CustomerCompactDTO customer;
    private String description;
}