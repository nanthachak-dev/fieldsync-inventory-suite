package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.stock.movement_details.StockMovementDetailsResponseDTO;
import com.example.fieldsync_inventory_backend.entity.view.StockMovementDetailsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMovementDetailsMapper {
    StockMovementDetailsResponseDTO toResponseDTO(StockMovementDetailsEntity entity);
}
