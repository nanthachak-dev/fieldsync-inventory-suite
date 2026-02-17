package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchResponseDTO;
import com.example.fieldsync_inventory_backend.entity.inventory.SeedBatchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeedBatchMapper {
    SeedBatchResponseDTO toResponseDTO(SeedBatchEntity entity);
}
