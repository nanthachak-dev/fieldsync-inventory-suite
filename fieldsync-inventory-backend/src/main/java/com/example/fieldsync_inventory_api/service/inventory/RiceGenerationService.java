package com.example.fieldsync_inventory_api.service.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.rice_generation.RiceGenerationRequestDTO;
import com.example.fieldsync_inventory_api.dto.inventory.rice_generation.RiceGenerationResponseDTO;
import com.example.fieldsync_inventory_api.entity.inventory.RiceGenerationEntity;
import com.example.fieldsync_inventory_api.repository.inventory.RiceGenerationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiceGenerationService {

    private final RiceGenerationRepository repository;

    // Use separate DTOs for request and response
    private RiceGenerationResponseDTO mapToResponseDTO(RiceGenerationEntity riceGeneration) {
        return RiceGenerationResponseDTO.builder()
                .id(riceGeneration.getId())
                .name(riceGeneration.getName())
                .description(riceGeneration.getDescription())
                .createdAt(riceGeneration.getCreatedAt())
                .updatedAt(riceGeneration.getUpdatedAt())
                .deletedAt(riceGeneration.getDeletedAt())
                .build();
    }

    private RiceGenerationEntity mapToEntity(RiceGenerationRequestDTO dto) {
        // Only map fields that come from the client
        return RiceGenerationEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    // CREATE: Uses the Request DTO
    public RiceGenerationResponseDTO createRiceGeneration(RiceGenerationRequestDTO dto) {
        RiceGenerationEntity entity = mapToEntity(dto);
        // JPA Auditing will automatically set created_at and updated_at
        return mapToResponseDTO(repository.save(entity));
    }

    // UPDATE: Uses the Request DTO
    public RiceGenerationResponseDTO updateRiceGeneration(Integer id, RiceGenerationRequestDTO dto) {
        RiceGenerationEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        // JPA Auditing will automatically set updated_at
        return mapToResponseDTO(repository.save(existing));
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteRiceGeneration(Integer id) {
        repository.deleteById(id);
    }

    public RiceGenerationResponseDTO getRiceGenerationById(Integer id) {
        RiceGenerationEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<RiceGenerationResponseDTO> getAllRiceGenerations() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
