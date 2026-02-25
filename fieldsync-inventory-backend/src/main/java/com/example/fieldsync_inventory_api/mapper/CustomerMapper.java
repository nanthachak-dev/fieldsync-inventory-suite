package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.sales.customer.CustomerCompactDTO;
import com.example.fieldsync_inventory_api.dto.sales.customer.CustomerResponseDTO;
import com.example.fieldsync_inventory_api.entity.sales.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDTO toResponseDTO(CustomerEntity entity);
    CustomerCompactDTO toCompactResponseDTO(CustomerEntity entity);
}
