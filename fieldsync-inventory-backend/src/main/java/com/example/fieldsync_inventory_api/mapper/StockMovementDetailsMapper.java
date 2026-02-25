package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.stock.movement_details.StockMovementDetailsResponseDTO;
import com.example.fieldsync_inventory_api.entity.view.StockMovementDetailsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMovementDetailsMapper {
    StockMovementDetailsResponseDTO toResponseDTO(StockMovementDetailsEntity entity);
}
