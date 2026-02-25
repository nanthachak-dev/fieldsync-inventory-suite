package com.example.fieldsync_inventory_api.service.user;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.role.RoleRequestDTO;
import com.example.fieldsync_inventory_api.dto.user.role.RoleResponseDTO;
import com.example.fieldsync_inventory_api.entity.user.RoleEntity;
import com.example.fieldsync_inventory_api.repository.user.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    // Use separate DTOs for request and response
    private RoleResponseDTO mapToResponseDTO(RoleEntity variety) {
        return RoleResponseDTO.builder()
                .id(variety.getId())
                .name(variety.getName())
                .description(variety.getDescription())
                .createdAt(variety.getCreatedAt())
                .updatedAt(variety.getUpdatedAt())
                .deletedAt(variety.getDeletedAt())
                .build();
    }

    private RoleEntity mapToEntity(RoleRequestDTO dto) {
        // Only map fields that come from the client
        return RoleEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    // CREATE: Uses the Request DTO
    public RoleResponseDTO createRole(RoleRequestDTO dto) {
        RoleEntity entity = mapToEntity(dto);
        // JPA Auditing will automatically set created_at and updated_at
        return mapToResponseDTO(repository.save(entity));
    }

    // UPDATE: Uses the Request DTO
    public RoleResponseDTO updateRole(Integer id, RoleRequestDTO dto) {
        RoleEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        // JPA Auditing will automatically set updated_at
        return mapToResponseDTO(repository.save(existing));
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteRole(Integer id) {
        repository.deleteById(id);
    }

    public RoleResponseDTO getRoleById(Integer id) {
        RoleEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<RoleResponseDTO> getAllRoles() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}