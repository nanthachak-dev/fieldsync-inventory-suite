package com.example.fieldsync_inventory_backend.service.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase_item.PurchaseItemRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase_item.PurchaseItemResponseDTO;
import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseEntity;
import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseItemEntity;
import com.example.fieldsync_inventory_backend.entity.stock.StockMovementEntity;
import com.example.fieldsync_inventory_backend.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_backend.mapper.PurchaseItemMapper;
import com.example.fieldsync_inventory_backend.repository.procurement.PurchaseItemRepository;
import com.example.fieldsync_inventory_backend.repository.procurement.PurchaseRepository;
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
public class PurchaseItemService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseItemService.class);

    private final PurchaseItemRepository repository;
    private final StockMovementRepository stockMovementRepository;
    private final PurchaseRepository purchaseRepository;

    private final PurchaseItemMapper purchaseItemMapper;

    private PurchaseItemEntity mapToEntity(PurchaseItemRequestDTO dto) {
        PurchaseEntity purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + dto.getPurchaseId()));
        StockMovementEntity stockMovement = stockMovementRepository.findById(dto.getStockMovementId())
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement not found with id: " + dto.getStockMovementId()));

        return PurchaseItemEntity.builder()
                .stockMovement(stockMovement)
                .purchase(purchase)
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();
    }

    @Transactional
    public PurchaseItemResponseDTO createOrUpdatePurchaseItem(PurchaseItemRequestDTO dto) {
        PurchaseItemEntity validatedEntity = mapToEntity(dto);
        PurchaseItemEntity savedEntity;

        Optional<PurchaseItemEntity> existedPurchaseItem = repository.findById(dto.getStockMovementId());
        if (existedPurchaseItem.isPresent()) {
            logger.info("{}Update operation{}", ANSI_YELLOW, ANSI_RESET);

            PurchaseItemEntity updatePurchaseItem = existedPurchaseItem.get();

            updatePurchaseItem.setPurchase(validatedEntity.getPurchase());
            updatePurchaseItem.setPrice(validatedEntity.getPrice());
            updatePurchaseItem.setDescription(validatedEntity.getDescription());
            updatePurchaseItem.setDeletedAt(null);

            savedEntity = repository.save(updatePurchaseItem);
        } else {
            logger.info("{}Create-new operation{}", ANSI_YELLOW, ANSI_RESET);
            savedEntity = repository.save(validatedEntity);
        }

        PurchaseItemEntity fullyPopulatedEntity = repository.findById(savedEntity.getStockMovementId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return purchaseItemMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public PurchaseItemResponseDTO getPurchaseItemById(Long id) {
        PurchaseItemEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return purchaseItemMapper.toResponseDTO(entity);
    }

    public List<PurchaseItemResponseDTO> getAllPurchaseItems() {
        return repository.findAllActive()
                .stream()
                .map(purchaseItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseItemResponseDTO> getAllSoftDeletedPurchaseItems() {
        return repository.findAllDeleted()
                .stream()
                .map(purchaseItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseItemResponseDTO> getAllWithDeleted() {
        return repository.findAllWithDeleted()
                .stream()
                .map(purchaseItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deletePurchaseItem(Long id) {
        repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found or already deleted with id: " + id));
        repository.deleteById(id);
    }
}
