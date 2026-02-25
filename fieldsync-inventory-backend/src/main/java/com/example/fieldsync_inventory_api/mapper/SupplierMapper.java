package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.procurement.supplier.SupplierCompactDTO;
import com.example.fieldsync_inventory_api.dto.procurement.supplier.SupplierResponseDTO;
import com.example.fieldsync_inventory_api.entity.procurement.SupplierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierResponseDTO toResponseDTO(SupplierEntity entity);
    SupplierCompactDTO toCompactResponseDTO(SupplierEntity entity);
}
