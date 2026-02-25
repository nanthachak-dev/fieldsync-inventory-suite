package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.stock.movement.StockMovementResponseDTO;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    // To DTO
    StockMovementResponseDTO toResponseDTO(StockMovementEntity entity);
}
