package com.example.fieldsync_inventory_backend.service.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_condition.SeedConditionRequestDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_condition.SeedConditionResponseDTO;
import com.example.fieldsync_inventory_backend.entity.inventory.SeedConditionEntity;
import com.example.fieldsync_inventory_backend.repository.inventory.SeedConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeedConditionService {

    private final SeedConditionRepository repository;

    // Use separate DTOs for request and response
    private SeedConditionResponseDTO mapToResponseDTO(SeedConditionEntity variety) {
        return SeedConditionResponseDTO.builder()
                .id(variety.getId())
                .name(variety.getName())
                .description(variety.getDescription())
                .createdAt(variety.getCreatedAt())
                .updatedAt(variety.getUpdatedAt())
                .deletedAt(variety.getDeletedAt())
                .build();
    }

    private SeedConditionEntity mapToEntity(SeedConditionRequestDTO dto) {
        // Only map fields that come from the client
        return SeedConditionEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    // CREATE: Uses the Request DTO
    public SeedConditionResponseDTO createSeedCondition(SeedConditionRequestDTO dto) {
        SeedConditionEntity entity = mapToEntity(dto);
        // JPA Auditing will automatically set created_at and updated_at
        return mapToResponseDTO(repository.save(entity));
    }

    // UPDATE: Uses the Request DTO
    public SeedConditionResponseDTO updateSeedCondition(Integer id, SeedConditionRequestDTO dto) {
        SeedConditionEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        // JPA Auditing will automatically set updated_at
        return mapToResponseDTO(repository.save(existing));
    }

    public void deleteSeedCondition(Integer id) {
        repository.deleteById(id);
    }

    public SeedConditionResponseDTO getSeedConditionById(Integer id) {
        SeedConditionEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<SeedConditionResponseDTO> getAllSeedConditions() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}