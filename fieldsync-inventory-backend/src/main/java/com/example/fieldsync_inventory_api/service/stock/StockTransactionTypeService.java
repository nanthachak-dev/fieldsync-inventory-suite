package com.example.fieldsync_inventory_api.service.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.stock.transaction_type.StockTransactionTypeRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_type.StockTransactionTypeResponseDTO;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionTypeEntity;
import com.example.fieldsync_inventory_api.repository.stock.StockTransactionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockTransactionTypeService {

    private final StockTransactionTypeRepository repository;

    // Use separate DTOs for request and response
    private StockTransactionTypeResponseDTO mapToResponseDTO(StockTransactionTypeEntity entity) {
        return StockTransactionTypeResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private StockTransactionTypeEntity mapToEntity(StockTransactionTypeRequestDTO dto) {
        // Only map fields that come from the client
        return StockTransactionTypeEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public StockTransactionTypeResponseDTO createStockTransactionType(StockTransactionTypeRequestDTO dto) {
        StockTransactionTypeEntity entity = mapToEntity(dto);
        return mapToResponseDTO(repository.save(entity));
    }

    public StockTransactionTypeResponseDTO getStockTransactionTypeById(Integer id) {
        StockTransactionTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    public List<StockTransactionTypeResponseDTO> getAllStockTransactionTypes() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public StockTransactionTypeResponseDTO updateStockTransactionType(Integer id, StockTransactionTypeRequestDTO dto) {
        StockTransactionTypeEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        return mapToResponseDTO(repository.save(existing));
    }

    public void deleteStockTransactionType(Integer id) {
        repository.deleteById(id);
    }
}
