package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.sales.sale.SaleResponseDTO;
import com.example.fieldsync_inventory_api.entity.sales.SaleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    // To DTO
    SaleResponseDTO toResponseDTO(SaleEntity entity);
}
