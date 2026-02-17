package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.sales.customer.CustomerCompactDTO;
import com.example.fieldsync_inventory_backend.dto.sales.customer.CustomerResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDTO toResponseDTO(CustomerEntity entity);
    CustomerCompactDTO toCompactResponseDTO(CustomerEntity entity);
}
