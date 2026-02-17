package com.example.fieldsync_inventory_backend.service.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.sale.SaleRequestDTO;
import com.example.fieldsync_inventory_backend.dto.sales.sale.SaleResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.CustomerEntity;
import com.example.fieldsync_inventory_backend.entity.sales.SaleEntity;
import com.example.fieldsync_inventory_backend.entity.stock.StockTransactionEntity;
import com.example.fieldsync_inventory_backend.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_backend.mapper.SaleMapper;
import com.example.fieldsync_inventory_backend.repository.sales.CustomerRepository;
import com.example.fieldsync_inventory_backend.repository.sales.SaleRepository;
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
public class SaleService {

    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    private final SaleRepository repository;
    private final StockTransactionRepository stockTransactionRepository;
    private final CustomerRepository customerRepository;

    private final SaleMapper saleMapper;

    // Helper method to map DTO to Entity
    private SaleEntity mapToEntity(SaleRequestDTO dto) {
        StockTransactionEntity stockTransaction = stockTransactionRepository.findById(dto.getStockTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " +
                        dto.getStockTransactionId()));

        CustomerEntity customer = null;
        if (dto.getCustomerId() != null) {
            customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " +
                            dto.getCustomerId()));
        }

        return SaleEntity.builder()
                .stockTransaction(stockTransaction)
                .customer(customer)
                .description(dto.getDescription())
                .build();
    }

    // This method is transactional to ensure both entities are handled correctly.
    @Transactional
    public SaleResponseDTO createOrUpdateSale(SaleRequestDTO dto) {
        SaleEntity validatedEntity = mapToEntity(dto);
        SaleEntity savedEntity;

        // Find if sale's transactionId exists for update operation
        Optional<SaleEntity> existSale = repository.findById(dto.getStockTransactionId());
        if (existSale.isPresent()) { // Update operation
            logger.info("{}Update operation{}", ANSI_YELLOW, ANSI_RESET);

            SaleEntity updateSale = existSale.get();

            updateSale.setCustomer(validatedEntity.getCustomer());
            updateSale.setDescription(validatedEntity.getDescription());
            updateSale.setDeletedAt(null);

            // Save to database
            savedEntity = repository.save(updateSale);

        } else { // Creating new record operation
            logger.info("{}Create-new operation{}", ANSI_YELLOW, ANSI_RESET);

            // Save to database
            savedEntity = repository.save(validatedEntity);
        }

        // Retrieve the fully populated entity (if your save doesn't return it)
        SaleEntity fullyPopulatedEntity = repository.findById(savedEntity.getStockTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return saleMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public SaleResponseDTO getSaleById(Long id) {
        SaleEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return saleMapper.toResponseDTO(entity);
    }

    public List<SaleResponseDTO> getAllSales() {
        return repository.findAllActive()
                .stream()
                .map(saleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SaleResponseDTO> getAllSoftDeletedSales() {
        return repository.findAllDeleted()
                .stream()
                .map(saleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SaleResponseDTO> getAllWithDeleted() {
        return repository.findAllWithDeleted()
                .stream()
                .map(saleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // SOFT DELETE: Uses the @SQLDelete on the entity
    public void deleteSale(Long id) {
        // Find the record, but only if it's currently active (deleted_at is NULL).
        // If it's already soft-deleted, this method will not find it.
        SaleEntity existing = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found" +
                        " or already deleted with id: " + id));

        // If the record is found (and is active), proceed with the soft-delete.
        // This call will trigger the @SQLDelete update.
        repository.deleteById(id);
    }
}