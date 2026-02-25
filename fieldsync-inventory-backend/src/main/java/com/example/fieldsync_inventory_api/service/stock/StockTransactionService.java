package com.example.fieldsync_inventory_api.service.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionResponseDTO;
import com.example.fieldsync_inventory_api.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionTypeEntity;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.mapper.StockTransactionMapper;
import com.example.fieldsync_inventory_api.repository.user.AppUserRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockTransactionRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockTransactionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockTransactionService {

    private final StockTransactionRepository repository;
    private final StockTransactionTypeRepository transactionTypeRepository;
    private final AppUserRepository userRepository;

    // Mapper
    private final StockTransactionMapper stockTransactionMapper;

    private StockTransactionEntity mapToEntity(StockTransactionRequestDTO dto){

        // Validate and fetch the related entities.
        StockTransactionTypeEntity transactionType = transactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction type not found with id: " +
                        dto.getTransactionTypeId()));

        AppUserEntity user = userRepository.findById(dto.getPerformedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
                        dto.getTransactionTypeId()));

        return StockTransactionEntity.builder()
                .transactionType(transactionType)
                .performedBy(user)
                .transactionDate(dto.getTransactionDate())
                .description(dto.getDescription())
                .build();
    }

    public StockTransactionResponseDTO createStockTransaction(StockTransactionRequestDTO dto) {
        StockTransactionEntity validatedEntity = mapToEntity(dto);

        // Save new entity
        StockTransactionEntity savedEntity = repository.save(validatedEntity);

        // Retrieve the fully populated entity (if your save doesn't return it)
        StockTransactionEntity fullyPopulatedEntity = repository.findActiveById(savedEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return stockTransactionMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public StockTransactionResponseDTO updateStockTransaction(Long id, StockTransactionRequestDTO dto) {
        StockTransactionEntity existing = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock transaction not found or deleted with id: " + id));

        // Get the new, valid entity data
        StockTransactionEntity validatedEntity = mapToEntity(dto);

        // Set the properties directly on the existing object.
        existing.setTransactionType(validatedEntity.getTransactionType());
        existing.setPerformedBy(validatedEntity.getPerformedBy());
        existing.setTransactionDate(validatedEntity.getTransactionDate());
        existing.setDescription(validatedEntity.getDescription());

        // Save the updated entity
        StockTransactionEntity savedEntity = repository.save(existing);

        // return stockTransactionMapper.toResponseDTO(savedEntity);
        // Retrieve the fully populated entity (if your save doesn't return it)
        StockTransactionEntity fullyPopulatedEntity = repository.findActiveById(savedEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve updated record"));

        return stockTransactionMapper.toResponseDTO(fullyPopulatedEntity);
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteStockTransaction(Long id) {
        // Find the record, but only if it's currently active (deleted_at is NULL).
        // If it's already soft-deleted, this method will not find it.
        StockTransactionEntity existing = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found" +
                        " or already deleted with id: " + id));

        // If the record is found (and is active), proceed with the soft-delete.
        // This call will trigger the @SQLDelete update.
        repository.deleteById(id);
    }

    public StockTransactionResponseDTO getStockTransactionById(Long id) {
        StockTransactionEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return stockTransactionMapper.toResponseDTO(entity);
    }

    public List<StockTransactionResponseDTO> getAllStockTransactions() {
        return repository.findAllActive()
                .stream()
                .map(stockTransactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockTransactionResponseDTO> getAllSoftDeletedStockTransactions() {
        return repository.findAllDeleted()
                .stream()
                .map(stockTransactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockTransactionResponseDTO> getAllWithDeleted() {
        return repository.findAllWithDeleted()
                .stream()
                .map(stockTransactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
