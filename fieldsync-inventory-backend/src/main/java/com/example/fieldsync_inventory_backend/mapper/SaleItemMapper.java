package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.sales.sale_item.SaleItemResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.SaleItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {
    // To DTO
    SaleItemResponseDTO toResponseDTO(SaleItemEntity entity);
}
