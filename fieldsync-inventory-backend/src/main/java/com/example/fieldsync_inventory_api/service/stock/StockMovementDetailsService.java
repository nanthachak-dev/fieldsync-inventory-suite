package com.example.fieldsync_inventory_api.service.stock;

import com.example.fieldsync_inventory_api.dto.stock.summary.StockSummaryResponseDTO;
import com.example.fieldsync_inventory_api.dto.stock.summary.StockVarietySummaryResponseDTO;
import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.PagedResponse;
import com.example.fieldsync_inventory_api.dto.stock.movement_details.RiceVarietySaleResponseDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_details.RiceVarietyStockResponseDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_details.StockMovementDetailsResponseDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_details.SyncRecordDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement_details.TotalStockResponseDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.StockMovementTOResDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.TOSaleResDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.TransactionOperationResDTO;
import com.example.fieldsync_inventory_api.entity.sales.CustomerEntity;
import com.example.fieldsync_inventory_api.entity.sales.SaleEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionTypeEntity;
import com.example.fieldsync_inventory_api.entity.view.StockMovementDetailsEntity;
import com.example.fieldsync_inventory_api.entity.view.StockMovementDetailsId;
import com.example.fieldsync_inventory_api.enums.SyncAction;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.mapper.CustomerMapper;
import com.example.fieldsync_inventory_api.mapper.StockMovementDetailsMapper;
import com.example.fieldsync_inventory_api.mapper.TransactionOperationMapper;
import com.example.fieldsync_inventory_api.repository.sales.CustomerRepository;
import com.example.fieldsync_inventory_api.repository.sales.SaleRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockMovementDetailsRepository;
import com.example.fieldsync_inventory_api.repository.stock.StockTransactionTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(StockMovementDetailsService.class);

    private final StockMovementDetailsRepository repository;
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final StockTransactionTypeRepository transactionTypeRepository;

    private final CustomerMapper customerMapper;
    private final StockMovementDetailsMapper mapper;

    /**
     * Retrieves a single transaction view record by its composite ID and maps it to
     * a DTO.
     * This method is suitable for getting details of a single stock movement within
     * a transaction.
     *
     * @param stockTransactionId The ID of the stock transaction.
     * @param stockMovementId    The ID of the stock movement.
     * @return The TransactionOperationResDTO for the single movement.
     * @throws ResourceNotFoundException if the record is not found.
     */
    public TransactionOperationResDTO getTransactionViewById(Long stockTransactionId,
            Long stockMovementId) {
        logger.info("Fetching single transaction view record with IDs: stockTransactionId={}, " +
                "stockMovementId={}", stockTransactionId, stockMovementId);

        StockMovementDetailsId id = new StockMovementDetailsId(stockTransactionId, stockMovementId);
        StockMovementDetailsEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction view record not found with ID: " + id));

        // Find transaction type description
        String transactionTypeDescription = getTransactionTypeDescription(entity);

        return TransactionOperationMapper.mapToResponseDTO(entity, transactionTypeDescription);
    }

    /**
     * API Endpoint: api/transaction-details
     *
     * @return The list of TransactionDetailsResponseDTO
     */
    public List<StockMovementDetailsResponseDTO> getAllStockMovementDetails() {
        return repository.findAllActive()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find transaction details by id
     */
    public List<StockMovementDetailsResponseDTO> getStockMovementDetails(Long id) {
        return repository.findByTransactionId(id)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SyncRecordDTO> getUpdatedAndDeletedSince(Instant lastSyncTime) {

        // Get New or Modified Records (where deletedAt is null)
        List<StockMovementDetailsEntity> changes = repository.findByCreatedAtAfterOrUpdatedAtAfter(lastSyncTime,
                lastSyncTime);

        // Get Deleted Records
        List<StockMovementDetailsEntity> deletions = repository.findByDeletedAtIsNotNullAndDeletedAtAfter(lastSyncTime);

        List<SyncRecordDTO> syncList = new ArrayList<>();

        // Classify changes (CREATED or UPDATED)
        for (StockMovementDetailsEntity entity : changes) {
            SyncAction action;

            // Use a small buffer (e.g., 1 millisecond) to prevent edge case duplicates
            // if creation and update happened at the exact same moment.
            if (entity.getCreatedAt().isAfter(lastSyncTime)) {
                action = SyncAction.CREATED;
            } else {
                // If not newly created, it must be updated (since it passed the updatedAtAfter
                // filter)
                action = SyncAction.UPDATED;
            }

            syncList.add(SyncRecordDTO.builder()
                    .action(action)
                    .record(mapper.toResponseDTO(entity)) // Assuming you have a mapper
                    .build());
        }

        // Classify deletions
        for (StockMovementDetailsEntity entity : deletions) {
            // For DELETED records, you only need to send the key fields (ID)
            // to tell the client what to remove. Sending the full DTO is optional.
            syncList.add(SyncRecordDTO.builder()
                    .action(SyncAction.DELETED)
                    .record(mapper.toResponseDTO(entity))
                    .build());
        }

        return syncList;
    }

    // Since TransactionViewEntity doesn't have field transactionTypeDescription but
    // TransactionOperationResDTO
    // requires it, so we have to workaround by creating descriptionMap to be able
    // to use entities.stream().map()
    // to transfer entities to transactionTypeDescription with descriptionMap
    // injected
    public List<TransactionOperationResDTO> getAllTransactionViews() {
        logger.info("Fetching all transaction view records.");

        // 1. Service uses Repository A (TransactionViewRepository)
        List<StockMovementDetailsEntity> entities = repository.findAll();

        // 2. Service uses Repository B (to get the descriptions)
        Map<Integer, String> descriptionMap = fetchDescriptionsForEntities(entities);

        // 3. Service uses a Lambda to map, providing ALL data needed
        return entities.stream()
                .map(entity -> {
                    String description = descriptionMap.get(entity.getTransactionTypeId());
                    // Pass both arguments to the existing mapper method
                    return TransactionOperationMapper.mapToResponseDTO(entity, description);
                })
                .collect(Collectors.toList());
    }

    // Helper to get transaction type description

    /**
     * Fetches descriptions for all unique transaction types present in the list of
     * view entities.
     *
     * @param entities The list of TransactionViewEntity.
     * @return A Map where Key is the Transaction Type ID (Long) and Value is the
     *         description (String).
     */
    private Map<Integer, String> fetchDescriptionsForEntities(List<StockMovementDetailsEntity> entities) {

        // 1. Extract all unique Transaction Type IDs from the entities
        Set<Integer> typeIds = entities.stream()
                .map(StockMovementDetailsEntity::getTransactionTypeId)
                .collect(Collectors.toSet());

        // 2. Fetch the corresponding TransactionType entities from the database
        // (This uses a single efficient query like `SELECT * FROM transaction_type
        // WHERE id IN (...)`)
        List<StockTransactionTypeEntity> typeEntities = transactionTypeRepository.findAllById(typeIds);

        // 3. Convert the list of entities into a Map for O(1) lookup
        return typeEntities.stream()
                .collect(Collectors.toMap(
                        StockTransactionTypeEntity::getId,
                        StockTransactionTypeEntity::getDescription));
    }

    public TransactionOperationResDTO getTransactionByTransactionId(Long stockTransactionId) {
        logger.info("Fetching aggregated transaction view for stock transaction ID: {}", stockTransactionId);

        List<StockMovementDetailsEntity> entities = repository.findByTransactionId(stockTransactionId);

        if (entities.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No transaction records found for stock transaction ID: " + stockTransactionId);
        }

        // The first entity has the main transaction details (assuming they are
        // consistent across all rows)
        StockMovementDetailsEntity mainEntity = entities.getFirst();

        // Find transaction type description
        String transactionTypeDescription = getTransactionTypeDescription(mainEntity);

        TransactionOperationResDTO responseDTO = TransactionOperationResDTO.builder()
                .transactionId(mainEntity.getTransactionId())
                .transactionType(TransactionOperationMapper.mapToStockTransactionTypeCompactDTO(mainEntity,
                        transactionTypeDescription))
                .performedBy(TransactionOperationMapper.mapToAppUserCompactDTO(mainEntity))
                .transactionDate(mainEntity.getTransactionDate())
                .description(mainEntity.getTransactionDescription())
                .build();

        // Populate the list of stock movements and calculate sumSale
        ArrayList<StockMovementTOResDTO> stockMovements = new ArrayList<>();
        BigDecimal sumSale = BigDecimal.ZERO;
        for (StockMovementDetailsEntity entity : entities) {
            stockMovements.add(TransactionOperationMapper.mapToStockMovementTOResDTO(entity));
            if (entity.getSaleItemPrice() != null) {
                sumSale = sumSale.add(entity.getSaleItemPrice());
            }
        }
        responseDTO.setStockMovements(stockMovements);

        return responseDTO;
    }

    public TransactionOperationResDTO getActiveTransactionByTransactionId(Long stockTransactionId) {
        logger.info("Fetching active aggregated transaction view for stock transaction ID: {}",
                stockTransactionId);

        List<StockMovementDetailsEntity> entities = repository.findByTransactionId(stockTransactionId);

        if (entities.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No transaction records found for stock transaction ID: " + stockTransactionId);
        }

        // Check if the main transaction is soft-deleted
        StockMovementDetailsEntity mainEntity = entities.getFirst();
        if (mainEntity.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Transaction with ID " + stockTransactionId + " is already deleted.");
        }

        // Find transaction type description
        String transactionTypeDescription = getTransactionTypeDescription(mainEntity);

        // All good, proceed with aggregation
        TransactionOperationResDTO responseDTO = TransactionOperationResDTO.builder()
                .transactionId(mainEntity.getTransactionId())
                .transactionType(TransactionOperationMapper.mapToStockTransactionTypeCompactDTO(mainEntity,
                        transactionTypeDescription))
                .performedBy(TransactionOperationMapper.mapToAppUserCompactDTO(mainEntity))
                .transactionDate(mainEntity.getTransactionDate())
                .description(mainEntity.getTransactionDescription())
                .build();

        // Populate the list of stock movements and calculate sumSale
        ArrayList<StockMovementTOResDTO> stockMovements = new ArrayList<>();
        BigDecimal sumSale = BigDecimal.ZERO;
        for (StockMovementDetailsEntity entity : entities) {
            stockMovements.add(TransactionOperationMapper.mapToStockMovementTOResDTO(entity));
            if (entity.getSaleItemPrice() != null) {
                sumSale = sumSale.add(entity.getSaleItemPrice());
            }
        }

        TOSaleResDTO saleDTO = processSale(mainEntity, sumSale);

        responseDTO.setSale(saleDTO);
        responseDTO.setStockMovements(stockMovements);

        return responseDTO;
    }

    // Generate TOSaleDTO
    private TOSaleResDTO processSale(StockMovementDetailsEntity entity, BigDecimal sumSale) {
        // Find sale
        Optional<SaleEntity> findSale = saleRepository.findActiveById(entity.getSaleId());
        if (findSale.isEmpty()) {
            logger.info("This transaction (id:{}) has no sale record", entity.getSaleId());
            return null;
        }

        // Find customer
        CustomerEntity customer = null;
        if (entity.getCustomerId() != null) {
            Optional<CustomerEntity> findCustomer = customerRepository.findById(entity.getCustomerId());
            if (findCustomer.isPresent())
                customer = findCustomer.get();
        }

        return TOSaleResDTO.builder()
                .customer(customerMapper.toCompactResponseDTO(customer))
                .sumSale(sumSale)
                .build();
    }

    public TotalStockResponseDTO getTotalStock(Instant lastDate) {
        if (lastDate == null) {
            lastDate = Instant.now();
        }

        BigDecimal totalStock = repository.getTotalStockBeforeDate(lastDate);

        // Handle case where totalStock might be null (no records found)
        if (totalStock == null) {
            totalStock = BigDecimal.ZERO;
        }

        return TotalStockResponseDTO.builder()
                .totalStock(totalStock)
                .asOfDate(lastDate)
                .build();
    }

    public StockSummaryResponseDTO getStockSummary(
            Instant lastDate) {
        if (lastDate == null) {
            lastDate = Instant.now();
        }
        return repository.getStockSummaryBeforeDate(lastDate);
    }

    public PagedResponse<StockVarietySummaryResponseDTO> getStockVarietySummary(
            Instant lastDate, Pageable pageable) {
        if (lastDate == null) {
            lastDate = Instant.now();
        }
        return PagedResponse.fromPage(repository.getStockVarietySummaryBeforeDate(lastDate, pageable));
    }

    public PagedResponse<RiceVarietyStockResponseDTO> getRiceVarietyStock(Instant lastDate, Pageable pageable) {
        if (lastDate == null) {
            lastDate = Instant.now();
        }

        return PagedResponse.fromPage(repository.findRiceVarietyStockBeforeDate(lastDate, pageable));
    }

    public PagedResponse<RiceVarietySaleResponseDTO> getTopSellingVarieties(Instant startDate, Instant endDate,
            Pageable pageable) {
        if (endDate == null) {
            endDate = Instant.now();
        }
        if (startDate == null) {
            startDate = endDate.minus(30, java.time.temporal.ChronoUnit.DAYS);
        }

        return PagedResponse.fromPage(repository.findTopSellingVarietiesByDateRange(startDate, endDate, pageable));
    }

    // Helper: get transaction type description
    private String getTransactionTypeDescription(StockMovementDetailsEntity entity) {
        StockTransactionTypeEntity existingTransactionType = transactionTypeRepository
                .findById(entity.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction type not found with id: "
                        + entity.getTransactionTypeId()));
        return existingTransactionType.getDescription();
    }
}
