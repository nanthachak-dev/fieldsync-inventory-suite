package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.stock.movement.StockMovementResponseDTO;
import com.example.fieldsync_inventory_backend.entity.stock.StockMovementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    // To DTO
    StockMovementResponseDTO toResponseDTO(StockMovementEntity entity);
}
