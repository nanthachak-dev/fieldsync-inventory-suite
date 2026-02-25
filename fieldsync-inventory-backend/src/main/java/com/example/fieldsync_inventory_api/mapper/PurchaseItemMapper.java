package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.procurement.purchase_item.PurchaseItemResponseDTO;
import com.example.fieldsync_inventory_api.entity.procurement.PurchaseItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseItemMapper {
    PurchaseItemResponseDTO toResponseDTO(PurchaseItemEntity entity);
}
