package com.example.fieldsync_inventory_backend.service.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.season.SeasonRequestDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.season.SeasonResponseDTO;
import com.example.fieldsync_inventory_backend.entity.inventory.SeasonEntity;
import com.example.fieldsync_inventory_backend.repository.inventory.SeasonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository repository;

    // Use separate DTOs for request and response
    private SeasonResponseDTO mapToResponseDTO(SeasonEntity entity) {
        return SeasonResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private SeasonEntity mapToEntity(SeasonRequestDTO dto) {
        // Only map fields that come from the client
        return SeasonEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    // CREATE: Uses the Request DTO
    public SeasonResponseDTO createSeason(SeasonRequestDTO dto) {
        SeasonEntity entity = mapToEntity(dto);
        // JPA Auditing will automatically set created_at and updated_at
        return mapToResponseDTO(repository.save(entity));
    }

    // UPDATE: Uses the Request DTO
    public SeasonResponseDTO updateSeason(Integer id, SeasonRequestDTO dto) {
        SeasonEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        // JPA Auditing will automatically set updated_at
        return mapToResponseDTO(repository.save(existing));
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteSeason(Integer id) {
        repository.deleteById(id);
    }

    public SeasonResponseDTO getSeasonById(Integer id) {
        SeasonEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<SeasonResponseDTO> getAllSeasons() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
