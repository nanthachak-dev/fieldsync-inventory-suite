package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.user.user.AppUserCompactDTO;
import com.example.fieldsync_inventory_api.dto.sales.customer.CustomerCompactDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_type.StockTransactionTypeCompactDTO;
import com.example.fieldsync_inventory_api.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_api.entity.sales.CustomerEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class StockTransactionOperationMapper {

    public StockTransactionTypeCompactDTO toTypeCompactDTO(StockTransactionTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        return StockTransactionTypeCompactDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public AppUserCompactDTO toAppUserCompactDTO(AppUserEntity entity) {
        if (entity == null) {
            return null;
        }
        return AppUserCompactDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .build();
    }

    public CustomerCompactDTO toCustomerCompactDTO(CustomerEntity entity){
        if (entity == null) {
            return null;
        }
        return CustomerCompactDTO.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .build();
    }
}
