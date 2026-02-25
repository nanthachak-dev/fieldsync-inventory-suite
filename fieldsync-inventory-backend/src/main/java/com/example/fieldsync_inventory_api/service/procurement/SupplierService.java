package com.example.fieldsync_inventory_api.service.procurement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.procurement.supplier.SupplierRequestDTO;
import com.example.fieldsync_inventory_api.dto.procurement.supplier.SupplierResponseDTO;
import com.example.fieldsync_inventory_api.entity.procurement.SupplierEntity;
import com.example.fieldsync_inventory_api.entity.procurement.SupplierTypeEntity;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.mapper.SupplierMapper;
import com.example.fieldsync_inventory_api.repository.procurement.SupplierRepository;
import com.example.fieldsync_inventory_api.repository.procurement.SupplierTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository repository;
    private final SupplierTypeRepository supplierTypeRepository;
    private final SupplierMapper supplierMapper;

    @PersistenceContext
    private EntityManager entityManager;

    private SupplierEntity mapToEntity(SupplierRequestDTO dto) {
        SupplierTypeEntity supplierType = supplierTypeRepository.findById(dto.getSupplierTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier type not found with id: " + dto.getSupplierTypeId()));

        return SupplierEntity.builder()
                .supplierType(supplierType)
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .build();
    }

    public SupplierResponseDTO createSupplier(SupplierRequestDTO dto) {
        SupplierEntity entity = mapToEntity(dto);
        SupplierEntity saved = repository.save(entity);
        SupplierEntity fetched = repository.findById(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));
        return supplierMapper.toResponseDTO(fetched);
    }

    public SupplierResponseDTO getSupplierById(Integer id) {
        SupplierEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toResponseDTO(entity);
    }

    public List<SupplierResponseDTO> getAllSuppliers() {
        return repository.findAll()
                .stream()
                .map(supplierMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SupplierResponseDTO updateSupplier(Integer id, SupplierRequestDTO dto) {
        SupplierEntity existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        SupplierEntity validated = mapToEntity(dto);
        existing.setSupplierType(validated.getSupplierType());
        existing.setFullName(validated.getFullName());
        existing.setEmail(validated.getEmail());
        existing.setPhone(validated.getPhone());
        existing.setAddress(validated.getAddress());
        existing.setDescription(validated.getDescription());

        SupplierEntity saved = repository.save(existing);
        entityManager.clear();
        SupplierEntity fetched = repository.findById(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve updated record"));
        return supplierMapper.toResponseDTO(fetched);
    }

    public void deleteSupplier(Integer id) {
        repository.deleteById(id);
    }
}
