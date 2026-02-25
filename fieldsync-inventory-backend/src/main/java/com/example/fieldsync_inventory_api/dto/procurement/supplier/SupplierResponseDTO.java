package com.example.fieldsync_inventory_api.dto.procurement.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.procurement.supplier_type.SupplierTypeResponseDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponseDTO {
    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String description;
    private SupplierTypeResponseDTO supplierType;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
