package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.sales.sale_item.SaleItemResponseDTO;
import com.example.fieldsync_inventory_api.entity.sales.SaleItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {
    // To DTO
    SaleItemResponseDTO toResponseDTO(SaleItemEntity entity);
}
