package com.example.fieldsync_inventory_backend.service.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.rice_variety.RiceVarietyRequestDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.rice_variety.RiceVarietyResponseDTO;
import com.example.fieldsync_inventory_backend.entity.inventory.RiceVarietyEntity;
import com.example.fieldsync_inventory_backend.repository.inventory.RiceVarietyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiceVarietyService {
    private final RiceVarietyRepository repository;

    // Use separate DTOs for request and response
    private RiceVarietyResponseDTO mapToResponseDTO(RiceVarietyEntity variety) {
        return RiceVarietyResponseDTO.builder()
                .id(variety.getId())
                .name(variety.getName())
                .description(variety.getDescription())
                .imageUrl(variety.getImageUrl())
                .createdAt(variety.getCreatedAt())
                .updatedAt(variety.getUpdatedAt())
                .deletedAt(variety.getDeletedAt())
                .build();
    }

    private RiceVarietyEntity mapToEntity(RiceVarietyRequestDTO dto) {
        // Only map fields that come from the client
        return RiceVarietyEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    // CREATE: Uses the Request DTO
    public RiceVarietyResponseDTO createRiceVariety(RiceVarietyRequestDTO dto) {
        RiceVarietyEntity entity = mapToEntity(dto);
        // JPA Auditing will automatically set created_at and updated_at
        return mapToResponseDTO(repository.save(entity));
    }

    // UPDATE: Uses the Request DTO
    public RiceVarietyResponseDTO updateRiceVariety(Integer id, RiceVarietyRequestDTO dto) {
        RiceVarietyEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());

        // JPA Auditing will automatically set updated_at
        return mapToResponseDTO(repository.save(existing));
    }

    public void deleteRiceVariety(Integer id) {
        repository.deleteById(id);
    }

    public RiceVarietyResponseDTO getRiceVarietyById(Integer id) {
        RiceVarietyEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<RiceVarietyResponseDTO> getAllRiceVarieties() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
