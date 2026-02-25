package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.procurement.purchase.PurchaseResponseDTO;
import com.example.fieldsync_inventory_api.entity.procurement.PurchaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseResponseDTO toResponseDTO(PurchaseEntity entity);
}
