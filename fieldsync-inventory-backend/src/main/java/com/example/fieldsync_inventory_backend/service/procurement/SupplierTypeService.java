package com.example.fieldsync_inventory_backend.service.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier_type.SupplierTypeRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier_type.SupplierTypeResponseDTO;
import com.example.fieldsync_inventory_backend.entity.procurement.SupplierTypeEntity;
import com.example.fieldsync_inventory_backend.repository.procurement.SupplierTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierTypeService {

    private final SupplierTypeRepository repository;

    private SupplierTypeResponseDTO mapToResponseDTO(SupplierTypeEntity entity) {
        return SupplierTypeResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private SupplierTypeEntity mapToEntity(SupplierTypeRequestDTO dto) {
        return SupplierTypeEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public SupplierTypeResponseDTO createSupplierType(SupplierTypeRequestDTO dto) {
        SupplierTypeEntity entity = mapToEntity(dto);
        return mapToResponseDTO(repository.save(entity));
    }

    public SupplierTypeResponseDTO getSupplierTypeById(Integer id) {
        SupplierTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<SupplierTypeResponseDTO> getAllSupplierTypes() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SupplierTypeResponseDTO updateSupplierType(Integer id, SupplierTypeRequestDTO dto) {
        SupplierTypeEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        return mapToResponseDTO(repository.save(existing));
    }

    public void deleteSupplierType(Integer id) {
        repository.deleteById(id);
    }
}