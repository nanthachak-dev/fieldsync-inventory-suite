package com.example.fieldsync_inventory_backend.dto.procurement.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCompactDTO {
    private Integer id;
    private String fullName;
}
