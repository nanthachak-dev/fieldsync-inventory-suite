package com.example.fieldsync_inventory_api.service.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.stock.movement.StockMovementRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement.StockMovementResponseDTO;
import com.example.fieldsync_inventory_api.entity.inventory.SeedBatchEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementTypeEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionEntity;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.mapper.StockMovementMapper;
import com.example.fieldsync_inventory_api.repository.inventory.SeedBatchRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockMovementRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockMovementTypeRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private static final Logger logger = LoggerFactory.getLogger(StockMovementService.class);

    private final StockMovementRepository repository;
    private final StockMovementTypeRepository movementTypeRepository;
    private final SeedBatchRepository seedBatchRepository;
    private final StockTransactionRepository stockTransactionRepository;

    // Mapper
    private final StockMovementMapper stockMovementMapper;

    private StockMovementEntity mapToEntity(StockMovementRequestDTO dto) {

        // Validate and fetch the related entities.
        StockMovementTypeEntity movementType = movementTypeRepository.findById(dto.getMovementTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Movement type not found with id: " +
                        dto.getMovementTypeId()));

        SeedBatchEntity seedBatch = seedBatchRepository.findById(dto.getSeedBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Seed batch not found with id: " +
                        dto.getSeedBatchId()));

        StockTransactionEntity stockTransaction = stockTransactionRepository.findById(dto.getStockTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock transaction not found with id: " +
                        dto.getStockTransactionId()));

        return StockMovementEntity.builder()
                .movementType(movementType)
                .seedBatch(seedBatch)
                .stockTransaction(stockTransaction)
                .quantity(dto.getQuantity())
                .description(dto.getDescription())
                .build();
    }

    public StockMovementResponseDTO createStockMovement(StockMovementRequestDTO dto) {
        logger.info("Creating record process with dto: {}", dto);

        StockMovementEntity validatedEntity = mapToEntity(dto);

        // Save new entity
        StockMovementEntity savedEntity = repository.save(validatedEntity);

        // Retrieve the fully populated entity (if your save doesn't return it)
        StockMovementEntity fullyPopulatedEntity = repository.findActiveById(savedEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return stockMovementMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public StockMovementResponseDTO updateStockMovement(Long id, StockMovementRequestDTO dto) {
        logger.info("Updating record process with dto: {}", dto);

        StockMovementEntity existing = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));

        // Get the new, valid entity data
        StockMovementEntity validatedEntity = mapToEntity(dto);

        // Step 3: Set the properties directly on the existing object.
        existing.setMovementType(validatedEntity.getMovementType());
        existing.setSeedBatch(validatedEntity.getSeedBatch());
        existing.setStockTransaction(validatedEntity.getStockTransaction());
        existing.setQuantity(validatedEntity.getQuantity());

        // Save the updated entity
        StockMovementEntity savedEntity = repository.save(existing);

        // return stockMovementMapper.toResponseDTO(savedEntity);
        // Retrieve the fully populated entity (if your save doesn't return it)
        StockMovementEntity fullyPopulatedEntity = repository.findActiveById(savedEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve updated record"));

        return stockMovementMapper.toResponseDTO(fullyPopulatedEntity);
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteStockMovement(Long id) {
        logger.info("Deleting record process with id: {}", id);

        // Find the record, but only if it's currently active (deleted_at is NULL).
        // If it's already soft-deleted, this method will not find it.
        StockMovementEntity existing = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found" +
                        " or already deleted with id: " + id));

        // If the record is found (and is active), proceed with the soft-delete.
        // This call will trigger the @SQLDelete update.
        repository.deleteById(id);
    }

    public StockMovementResponseDTO getStockMovementById(Long id) {
        StockMovementEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return stockMovementMapper.toResponseDTO(entity);
    }

    public List<StockMovementResponseDTO> getAllStockMovements() {
        return repository.findAllActive()
                .stream()
                .map(stockMovementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponseDTO> getAllSoftDeletedStockMovements() {
        return repository.findAllDeleted()
                .stream()
                .map(stockMovementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponseDTO> getAllWithDeleted() {
        return repository.findAllWithDeleted()
                .stream()
                .map(stockMovementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
