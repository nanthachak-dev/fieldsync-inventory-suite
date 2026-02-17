package com.example.fieldsync_inventory_backend.service.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.customer_type.CustomerTypeRequestDTO;
import com.example.fieldsync_inventory_backend.dto.sales.customer_type.CustomerTypeResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.CustomerTypeEntity;
import com.example.fieldsync_inventory_backend.repository.sales.CustomerTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerTypeService {

    private final CustomerTypeRepository repository;

    // Use separate DTOs for request and response
    private CustomerTypeResponseDTO mapToResponseDTO(CustomerTypeEntity entity) {
        return CustomerTypeResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private CustomerTypeEntity mapToEntity(CustomerTypeRequestDTO dto) {
        // Only map fields that come from the client
        return CustomerTypeEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public CustomerTypeResponseDTO createCustomerType(CustomerTypeRequestDTO dto) {
        CustomerTypeEntity entity = mapToEntity(dto);
        return mapToResponseDTO(repository.save(entity));
    }

    public CustomerTypeResponseDTO getCustomerTypeById(Integer id) {
        CustomerTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<CustomerTypeResponseDTO> getAllCustomerTypes() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerTypeResponseDTO updateCustomerType(Integer id, CustomerTypeRequestDTO dto) {
        CustomerTypeEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        return mapToResponseDTO(repository.save(existing));
    }

    public void deleteCustomerType(Integer id) {
        repository.deleteById(id);
    }
}
