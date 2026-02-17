package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.sales.sale.SaleResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.SaleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    // To DTO
    SaleResponseDTO toResponseDTO(SaleEntity entity);
}
