package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.procurement.purchase.PurchaseResponseDTO;
import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseResponseDTO toResponseDTO(PurchaseEntity entity);
}
