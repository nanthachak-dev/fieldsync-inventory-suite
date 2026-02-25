package com.example.fieldsync_inventory_api.mapper;

import com.example.fieldsync_inventory_api.dto.inventory.seed_batch.SeedBatchResponseDTO;
import com.example.fieldsync_inventory_api.entity.inventory.SeedBatchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeedBatchMapper {
    SeedBatchResponseDTO toResponseDTO(SeedBatchEntity entity);
}
