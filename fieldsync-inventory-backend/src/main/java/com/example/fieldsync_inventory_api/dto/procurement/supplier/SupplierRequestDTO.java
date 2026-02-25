package com.example.fieldsync_inventory_api.dto.procurement.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRequestDTO {
    private Integer supplierTypeId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String description;
}
