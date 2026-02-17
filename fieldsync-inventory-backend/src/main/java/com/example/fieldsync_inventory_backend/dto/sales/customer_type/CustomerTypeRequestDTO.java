package com.example.fieldsync_inventory_backend.dto.sales.customer_type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerTypeRequestDTO {
    private String name;
    private String description;
}
