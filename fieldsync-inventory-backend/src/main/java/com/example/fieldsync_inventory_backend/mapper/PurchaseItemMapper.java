package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.procurement.purchase_item.PurchaseItemResponseDTO;
import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseItemMapper {
    PurchaseItemResponseDTO toResponseDTO(PurchaseItemEntity entity);
}
