package com.example.fieldsync_inventory_backend.service.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.sale_item.SaleItemRequestDTO;
import com.example.fieldsync_inventory_backend.dto.sales.sale_item.SaleItemResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.SaleEntity;
import com.example.fieldsync_inventory_backend.entity.sales.SaleItemEntity;
import com.example.fieldsync_inventory_backend.entity.stock.StockMovementEntity;
import com.example.fieldsync_inventory_backend.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_backend.mapper.SaleItemMapper;
import com.example.fieldsync_inventory_backend.repository.sales.SaleItemRepository;
import com.example.fieldsync_inventory_backend.repository.sales.SaleRepository;
import com.example.fieldsync_inventory_backend.repository.stock.StockMovementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.fieldsync_inventory_backend.util.AppConstants.ANSI_RESET;
import static com.example.fieldsync_inventory_backend.util.AppConstants.ANSI_YELLOW;

@Service
@RequiredArgsConstructor
public class SaleItemService {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(SaleItemService.class);

    // Repositories
    private final SaleItemRepository repository;
    private final StockMovementRepository stockMovementRepository;
    private final SaleRepository saleRepository;

    private final SaleItemMapper saleItemMapper;

    // Helper method to map DTO to Entity
    private SaleItemEntity mapToEntity(SaleItemRequestDTO dto) {
        SaleEntity sale = saleRepository.findById(dto.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " +
                        dto.getSaleId()));
        StockMovementEntity stockMovement = stockMovementRepository.findById(dto.getStockMovementId())
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement not found with id: " +
                        dto.getStockMovementId()));

        return SaleItemEntity.builder()
                .stockMovement(stockMovement)
                .sale(sale)
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();
    }

    // This method is transactional to ensure both entities are handled correctly.
    @Transactional
    public SaleItemResponseDTO createOrUpdateSaleItem(SaleItemRequestDTO dto) {
        SaleItemEntity validatedEntity = mapToEntity(dto);
        SaleItemEntity savedEntity;

        // Find shared PK of StockMovement in SaleItem if it's been created
        Optional<SaleItemEntity> existedSaleItem = repository.findById(dto.getStockMovementId());
        if (existedSaleItem.isPresent()) { // Update operation
            logger.info("{}Update operation{}", ANSI_YELLOW, ANSI_RESET);

            SaleItemEntity updateSaleItem = existedSaleItem.get();

            updateSaleItem.setSale(validatedEntity.getSale());
            updateSaleItem.setPrice(validatedEntity.getPrice());
            updateSaleItem.setDescription(validatedEntity.getDescription());
            updateSaleItem.setDeletedAt(null);

            // Save to database
            savedEntity = repository.save(updateSaleItem);

        } else { // Creating new record operation
            logger.info("{}Create-new operation{}", ANSI_YELLOW, ANSI_RESET);

            // Save to database
            savedEntity = repository.save(validatedEntity);
        }

        // Retrieve the fully populated entity (if your save doesn't return it)
        SaleItemEntity fullyPopulatedEntity = repository.findById(savedEntity.getStockMovementId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return saleItemMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public SaleItemResponseDTO getSaleItemById(Long id) {
        SaleItemEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return saleItemMapper.toResponseDTO(entity);
    }

    public List<SaleItemResponseDTO> getAllSaleItems() {
        return repository.findAllActive()
                .stream()
                .map(saleItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SaleItemResponseDTO> getAllSoftDeletedSaleItems() {
        return repository.findAllDeleted()
                .stream()
                .map(saleItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SaleItemResponseDTO> getAllWithDeleted() {
        return repository.findAllWithDeleted()
                .stream()
                .map(saleItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteSaleItem(Long id) {
        // Find the record, but only if it's currently active (deleted_at is NULL).
        // If it's already soft-deleted, this method will not find it.
        SaleItemEntity existing = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found" +
                        " or already deleted with id: " + id));

        // If the record is found (and is active), proceed with the soft-delete.
        // This call will trigger the @SQLDelete update.
        repository.deleteById(id);
    }
}