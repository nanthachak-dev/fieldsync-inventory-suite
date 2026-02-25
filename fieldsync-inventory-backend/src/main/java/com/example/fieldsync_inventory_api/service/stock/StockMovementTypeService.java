package com.example.fieldsync_inventory_api.service.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.stock.movement_type.StockMovementTypeRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_type.StockMovementTypeResponseDTO;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementTypeEntity;
import com.example.fieldsync_inventory_api.enums.EffectOnStock;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.repository.stock.StockMovementTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementTypeService {

    private static final Logger logger = LoggerFactory.getLogger(StockMovementTypeService.class);

    private final StockMovementTypeRepository repository;

    // Use separate DTOs for request and response
    private StockMovementTypeResponseDTO mapToResponseDTO(StockMovementTypeEntity entity) {
        return StockMovementTypeResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .effectOnStock(entity.getEffectOnStock())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private StockMovementTypeEntity mapToEntity(StockMovementTypeRequestDTO dto) {
        try {
            // Only map fields that come from the client, with validation
            return StockMovementTypeEntity.builder()
                    .name(dto.getName())
                    .effectOnStock(EffectOnStock.valueOf(dto.getEffectOnStock().toUpperCase()))
                    .description(dto.getDescription())
                    .build();
        } catch (IllegalArgumentException e) {
            // If the conversion fails, throw a custom exception
            throw new ResourceNotFoundException("Invalid value '" + dto.getEffectOnStock() + "' for effectOnStock. Allowed values are: IN, OUT.");
        } catch (Exception e) {
            logger.error("Error while calling mapToEntity: {}", e.getMessage());
            throw new ResourceNotFoundException("Unknown request error");
        }
    }

    public StockMovementTypeResponseDTO createStockMovementType(StockMovementTypeRequestDTO dto) {
        Optional<StockMovementTypeEntity> existingNameEntity = repository.findByNameIgnoreCase(dto.getName());
        if (existingNameEntity.isPresent())
            throw new RuntimeException("Duplicated name: " + dto.getName());
        StockMovementTypeEntity entity = mapToEntity(dto);
        return mapToResponseDTO(repository.save(entity));
    }

    public StockMovementTypeResponseDTO getStockMovementTypeById(Integer id) {
        StockMovementTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<StockMovementTypeResponseDTO> getAllStockMovementTypes() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public StockMovementTypeResponseDTO updateStockMovementType(Integer id, StockMovementTypeRequestDTO dto) {
        StockMovementTypeEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        // Get the new, valid entity data
        StockMovementTypeEntity validatedEntity = mapToEntity(dto);

        existing.setName(validatedEntity.getName());
        existing.setEffectOnStock(validatedEntity.getEffectOnStock());
        existing.setDescription(validatedEntity.getDescription());

        return mapToResponseDTO(repository.save(existing));
    }

    public void deleteStockMovementType(Integer id) {
        repository.deleteById(id);
    }
}
