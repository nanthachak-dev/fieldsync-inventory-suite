package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.stock.transaction.StockTransactionResponseDTO;
import com.example.fieldsync_inventory_backend.entity.stock.StockTransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockTransactionMapper {
    // To DTO
    StockTransactionResponseDTO toResponseDTO(StockTransactionEntity entity);
}
