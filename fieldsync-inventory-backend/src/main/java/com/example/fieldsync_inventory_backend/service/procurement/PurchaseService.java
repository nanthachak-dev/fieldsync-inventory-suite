package com.example.fieldsync_inventory_backend.service.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase.PurchaseRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase.PurchaseResponseDTO;
import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseEntity;
import com.example.fieldsync_inventory_backend.entity.procurement.SupplierEntity;
import com.example.fieldsync_inventory_backend.entity.stock.StockTransactionEntity;
import com.example.fieldsync_inventory_backend.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_backend.mapper.PurchaseMapper;
import com.example.fieldsync_inventory_backend.repository.procurement.PurchaseRepository;
import com.example.fieldsync_inventory_backend.repository.procurement.SupplierRepository;
import com.example.fieldsync_inventory_backend.repository.stock.StockTransactionRepository;
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
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseRepository repository;
    private final StockTransactionRepository stockTransactionRepository;
    private final SupplierRepository supplierRepository;

    private final PurchaseMapper purchaseMapper;

    private PurchaseEntity mapToEntity(PurchaseRequestDTO dto) {
        StockTransactionEntity stockTransaction = stockTransactionRepository.findById(dto.getStockTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " +
                        dto.getStockTransactionId()));

        SupplierEntity supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " +
                            dto.getSupplierId()));
        }

        return PurchaseEntity.builder()
                .stockTransaction(stockTransaction)
                .supplier(supplier)
                .description(dto.getDescription())
                .build();
    }

    @Transactional
    public PurchaseResponseDTO createOrUpdatePurchase(PurchaseRequestDTO dto) {
        PurchaseEntity validatedEntity = mapToEntity(dto);
        PurchaseEntity savedEntity;

        Optional<PurchaseEntity> existPurchase = repository.findById(dto.getStockTransactionId());
        if (existPurchase.isPresent()) {
            logger.info("{}Update operation{}", ANSI_YELLOW, ANSI_RESET);

            PurchaseEntity updatePurchase = existPurchase.get();

            updatePurchase.setSupplier(validatedEntity.getSupplier());
            updatePurchase.setDescription(validatedEntity.getDescription());
            updatePurchase.setDeletedAt(null);

            savedEntity = repository.save(updatePurchase);

        } else {
            logger.info("{}Create-new operation{}", ANSI_YELLOW, ANSI_RESET);
            savedEntity = repository.save(validatedEntity);
        }

        PurchaseEntity fullyPopulatedEntity = repository.findById(savedEntity.getStockTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return purchaseMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public PurchaseResponseDTO getPurchaseById(Long id) {
        PurchaseEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return purchaseMapper.toResponseDTO(entity);
    }

    public List<PurchaseResponseDTO> getAllPurchases() {
        return repository.findAllActive()
                .stream()
                .map(purchaseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseResponseDTO> getAllSoftDeletedPurchases() {
        return repository.findAllDeleted()
                .stream()
                .map(purchaseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseResponseDTO> getAllWithDeleted() {
        return repository.findAllWithDeleted()
                .stream()
                .map(purchaseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deletePurchase(Long id) {
        repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found or already deleted with id: " + id));
        repository.deleteById(id);
    }
}
