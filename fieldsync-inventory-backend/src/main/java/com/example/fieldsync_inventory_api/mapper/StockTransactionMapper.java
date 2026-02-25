package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionResponseDTO;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockTransactionMapper {
    // To DTO
    StockTransactionResponseDTO toResponseDTO(StockTransactionEntity entity);
}
