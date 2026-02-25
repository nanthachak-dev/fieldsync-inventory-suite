package com.example.fieldsync_inventory_api.service.stock;

import com.example.fieldsync_inventory_api.dto.inventory.seed_batch.SeedBatchRequestDTO;
import com.example.fieldsync_inventory_api.dto.procurement.purchase_item.PurchaseItemRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.movement.StockMovementResponseDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.*;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in.TOStockMovementStockInReqDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in.TOStockMovementStockInResDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in.TransactionOperationStockInReqDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_operation.stock_in.TransactionOperationStockInResDTO;
import com.example.fieldsync_inventory_api.entity.inventory.RiceGenerationEntity;
import com.example.fieldsync_inventory_api.entity.inventory.RiceVarietyEntity;
import com.example.fieldsync_inventory_api.entity.inventory.SeasonEntity;
import com.example.fieldsync_inventory_api.entity.inventory.SeedBatchEntity;
import com.example.fieldsync_inventory_api.entity.procurement.PurchaseEntity;
import com.example.fieldsync_inventory_api.entity.procurement.PurchaseItemEntity;
import com.example.fieldsync_inventory_api.entity.procurement.SupplierEntity;
import com.example.fieldsync_inventory_api.entity.sales.CustomerEntity;
import com.example.fieldsync_inventory_api.entity.sales.SaleEntity;
import com.example.fieldsync_inventory_api.entity.sales.SaleItemEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementTypeEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionEntity;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionTypeEntity;
import com.example.fieldsync_inventory_api.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_api.entity.view.StockMovementDetailsEntity;
import com.example.fieldsync_inventory_api.enums.EffectOnStock;
import com.example.fieldsync_inventory_api.enums.OperationTask;
import com.example.fieldsync_inventory_api.enums.OperationType;
import com.example.fieldsync_inventory_api.exception.BadRequestException;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.mapper.StockMovementMapper;
import com.example.fieldsync_inventory_api.mapper.StockTransactionOperationMapper;
import com.example.fieldsync_inventory_api.mapper.TransactionOperationMapper;
import com.example.fieldsync_inventory_api.repository.inventory.RiceGenerationRepository;
import com.example.fieldsync_inventory_api.repository.inventory.RiceVarietyRepository;
import com.example.fieldsync_inventory_api.repository.inventory.SeasonRepository;
import com.example.fieldsync_inventory_api.repository.inventory.SeedBatchRepository;
import com.example.fieldsync_inventory_api.repository.procurement.PurchaseItemRepository;
import com.example.fieldsync_inventory_api.repository.procurement.PurchaseRepository;
import com.example.fieldsync_inventory_api.repository.procurement.SupplierRepository;
import com.example.fieldsync_inventory_api.repository.sales.CustomerRepository;
import com.example.fieldsync_inventory_api.repository.sales.SaleItemRepository;
import com.example.fieldsync_inventory_api.repository.sales.SaleRepository;
import com.example.fieldsync_inventory_api.repository.stock.*;
import com.example.fieldsync_inventory_api.repository.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.fieldsync_inventory_api.util.AppConstants.ANSI_RESET;
import static com.example.fieldsync_inventory_api.util.AppConstants.ANSI_YELLOW;

@Service
@RequiredArgsConstructor
public class TransactionOperationService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionOperationService.class);

    // Mappers
    private final StockMovementMapper movementMapper;
    private final StockTransactionOperationMapper transactionMapper;

    // Services
    private final StockMovementDetailsService transactionViewService;

    // Repositories
    private final StockTransactionRepository repository;
    private final StockTransactionTypeRepository transactionTypeRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SupplierRepository supplierRepository;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final CustomerRepository customerRepository;
    private final StockMovementRepository movementRepository;
    private final StockMovementTypeRepository movementTypeRepository;
    private final SeedBatchRepository batchRepository;
    private final RiceVarietyRepository varietyRepository;
    private final RiceGenerationRepository generationRepository;
    private final SeasonRepository seasonRepository;
    private final AppUserRepository userRepository;
    private final StockMovementDetailsRepository transactionDetailRepository;

    @Transactional
    public TransactionOperationResDTO createOperation(TransactionOperationReqDTO dto) {
        logger.info("{}Creating record process with dto: {}{}", ANSI_YELLOW, dto, ANSI_RESET);

        // Validate and fetch main entities
        TransactionEntities transactionEntities = validateAndFetchTransactionEntities(dto);

        // Create and save the core transaction
        StockTransactionEntity savedTransaction = createAndSaveTransaction(dto, transactionEntities);

        // Create and save the sale (if applicable)
        SaleEntity savedSale = createAndSaveSale(dto, savedTransaction, transactionEntities);

        // Process and save all stock movements
        ArrayList<StockMovementTOResDTO> stockMovementTOResDTOS = processStockMovementsForStockIn(dto,
                savedTransaction, savedSale);

        // Transaction operation record has been saved in a database. Next, generate response

        // Get sum of SaleItem for savedTransaction
        BigDecimal sumSale = BigDecimal.ZERO;
        for (StockMovementTOResDTO movementDTO : stockMovementTOResDTOS) {
            if (movementDTO.getSaleItem() != null && movementDTO.getSaleItem().getPrice() != null) {
                sumSale = sumSale.add(movementDTO.getSaleItem().getPrice());
            }
        }

        TOSaleResDTO saleResDTO = null;
        if (savedSale != null)
            saleResDTO = processSale(savedSale, sumSale);

        // Return the response DTO built from the saved data
        return TransactionOperationResDTO.builder()
                .transactionId(savedTransaction.getId())
                .transactionType(transactionMapper.toTypeCompactDTO(transactionEntities.transactionType()))
                .performedBy(transactionMapper.toAppUserCompactDTO(transactionEntities.user()))
                .sale(saleResDTO)
                .transactionDate(savedTransaction.getTransactionDate()) // Replace with transactionInstant
                .description(savedTransaction.getDescription())
                .stockMovements(stockMovementTOResDTOS)
                .build();
    }

    /**
     * @Endpoint: api/transaction-operations/stock-in
     * @param dto holds request data
     * @return response dto
     */
    @Transactional
    public TransactionOperationStockInResDTO createStockInOperation(TransactionOperationStockInReqDTO dto){
        // Get if a transaction type exists
        StockTransactionTypeEntity transactionType = transactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction type not found with name: " +
                        dto.getTransactionTypeId()));

        OperationType selectedOperation;

        try {
            selectedOperation = OperationType.valueOf(transactionType.getName()); // Error might occur if a list in enum doesn't match the database
        }catch (IllegalArgumentException e){
            throw new BadRequestException("Illegal operation:" + e.getMessage());
        }

        // Operation (transactionTypeName) must be STOCK_IN
        if (selectedOperation != OperationType.STOCK_IN)
            throw new BadRequestException("Unsupported operation");

        // Check if user exists
        AppUserEntity user = userRepository.findById(dto.getPerformedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
                        dto.getPerformedBy()));

        // ----------------
        // Purchase task
        // ----------------

        if (dto.getTask() == OperationTask.PURCHASE){
            logger.info("Create transaction operation for purchase");

            // Purchase must not be null
            if (dto.getPurchase() == null){
                throw new BadRequestException("Purchase data can't be null for PURCHASE task");
            }

            // Create and save transaction record
            StockTransactionEntity savedTransaction = createAndSaveTransaction(transactionType, user, dto.getTransactionDate(), dto.getDescription());

            // Create and save purchase data
            PurchaseEntity savedPurchase  = createAndSavePurchase(savedTransaction, dto.getPurchase().getSupplierId());
            String supplier = null;
            if (dto.getPurchase().getSupplierId() != null)
                supplier =savedPurchase.getSupplier().getFullName();

            // Create and save movement
            // Process and save all stock movements
            ArrayList<TOStockMovementStockInResDTO> movementResponseDTOs = processStockMovementsForStockIn(dto.getStockMovements(), savedTransaction, savedPurchase, dto.getTask());

            return TransactionOperationStockInResDTO.builder()
                    .transactionId(savedTransaction.getId())
                    .type(OperationType.STOCK_IN)
                    .task(OperationTask.PURCHASE)
                    .supplier(supplier)
                    .transactionDate(savedTransaction.getTransactionDate())
                    .stockMovements(movementResponseDTOs)
                    .description(savedTransaction.getDescription())
                    .performedBy(user.getUsername())
                    .build();
        }

        if (dto.getTask() == OperationTask.TRANSFER_IN){
            logger.info("Create transaction operation for transfer in");

            // Create and save transaction record
            StockTransactionEntity savedTransaction = createAndSaveTransaction(transactionType, user, dto.getTransactionDate(), dto.getDescription());

            // Create and save purchase data
            PurchaseEntity savedPurchase = null;
            if (dto.getPurchase() != null)
                savedPurchase = createAndSavePurchase(savedTransaction, dto.getPurchase().getSupplierId());
            String supplier = null;
            if (savedPurchase != null && dto.getPurchase().getSupplierId() != null)
                supplier =savedPurchase.getSupplier().getFullName();

            // Create and save movement
            // Process and save all stock movements
            ArrayList<TOStockMovementStockInResDTO> movementResponseDTOs = processStockMovementsForStockIn(dto.getStockMovements(), savedTransaction, savedPurchase, dto.getTask());

            return TransactionOperationStockInResDTO.builder()
                    .transactionId(savedTransaction.getId())
                    .type(OperationType.STOCK_IN)
                    .task(OperationTask.PURCHASE)
                    .supplier(supplier)
                    .transactionDate(savedTransaction.getTransactionDate())
                    .stockMovements(movementResponseDTOs)
                    .description(savedTransaction.getDescription())
                    .performedBy(user.getUsername())
                    .build();
        }

        // There might be other tasks besides Purchase
        throw new BadRequestException("Unsupported task: "+dto.getTask());
    }

    public TransactionOperationResDTO getById(Long transactionId) {
        logger.info("Fetching transaction by ID: {}", transactionId);
        // We use the TransactionViewService to get the full aggregated DTO.
        return transactionViewService.getActiveTransactionByTransactionId(transactionId);
    }

    public List<TransactionOperationResDTO> getAll() {
        logger.info("Fetching all active transactions.");

        // We get all active movements from the view.
        List<StockMovementDetailsEntity> movements = transactionDetailRepository.findAllActive();

        // Group the movements by their stock transaction ID.
        Map<Long, List<StockMovementDetailsEntity>> groupedByTransaction = movements.stream()
                .collect(Collectors.groupingBy(StockMovementDetailsEntity::getTransactionId));

        // For each group, build the aggregated DTO.
        return groupedByTransaction.values().stream()
                .map(this::buildTransactionOperationResDTO)
                .collect(Collectors.toList());
    }

    // Need update for transaction whose purchase and purchase item
    @Transactional
    public TransactionOperationResDTO updateOperation(Long id, TransactionOperationReqDTO dto) {
        StockTransactionEntity existingTransaction = repository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active transaction not found with id: " + id));

        // --- Update the parent StockTransaction entity ---
        StockTransactionTypeEntity transactionType = transactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction Type not found."));

        AppUserEntity user = userRepository.findById(dto.getPerformedById())
                .orElseThrow(() -> new IllegalArgumentException("App User not found."));

        existingTransaction.setTransactionType(transactionType);
        existingTransaction.setPerformedBy(user);
        existingTransaction.setTransactionDate(dto.getTransactionDate()); // Transaction Date should be converted to non-timezone from frontend before sending
        existingTransaction.setDescription(dto.getDescription());

        // --- Manage the child Sale entity first ---
        SaleEntity sale = null;
        Optional<SaleEntity> optionalSale = saleRepository.findActiveByStockTransactionId(id);
        if (dto.getSaleRequestDTO() != null) {
            if (optionalSale.isPresent()) {
                sale = optionalSale.get();
            } else {
                sale = new SaleEntity();
                sale.setStockTransaction(existingTransaction);
                sale.setStockTransactionId(existingTransaction.getId());
            }

            CustomerEntity customer = customerRepository.findById(dto.getSaleRequestDTO().getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found."));

            sale.setCustomer(customer);
            sale.setDescription(dto.getSaleRequestDTO().getDescription());
            saleRepository.save(sale);
        } else {
            if (optionalSale.isPresent()) {
                SaleEntity saleToDelete = optionalSale.get();
                List<SaleItemEntity> itemsToDelete = saleItemRepository.findBySaleId(saleToDelete
                        .getStockTransactionId());
                saleItemRepository.deleteAll(itemsToDelete);
                saleRepository.delete(saleToDelete);
            }
        }

        // --- Manage the child StockMovement and nested SaleItems ---
        List<StockMovementEntity> existingMovements = movementRepository.findActiveByStockTransactionId(id);

        Map<Long, StockMovementEntity> existingMovementsMap = existingMovements.stream()
                .collect(Collectors.toMap(StockMovementEntity::getId, Function.identity()));

        if (dto.getStockMovements() != null) {
            for (StockMovementTOReqDTO movementDTO : dto.getStockMovements()) {
                StockMovementEntity movementToUpdate;
                if (movementDTO.getId() != null) {
                    movementToUpdate = existingMovementsMap.get(movementDTO.getId());
                    if (movementToUpdate == null) {
                        throw new IllegalArgumentException("Invalid StockMovement ID provided for update: "
                                + movementDTO.getId());
                    }
                    existingMovementsMap.remove(movementDTO.getId());
                } else {
                    movementToUpdate = new StockMovementEntity();
                }

                // --- MANUAL MAPPING of StockMovement DTO to Entity ---
                StockMovementTypeEntity movementType = movementTypeRepository.findById(movementDTO
                                .getMovementTypeId())
                        .orElseThrow(() -> new IllegalArgumentException("Movement Type not found."));

                // *** CORRECTED LOGIC: Reuse the validation and findOrCreate helpers ***
                MovementEntities movementEntities = validateAndFetchMovementEntities(movementDTO);
                SeedBatchEntity seedBatch = findOrCreateSeedBatch(movementDTO, movementEntities);

                movementToUpdate.setMovementType(movementType);
                movementToUpdate.setSeedBatch(seedBatch);
                movementToUpdate.setQuantity(movementDTO.getQuantity());
                movementToUpdate.setDescription(movementDTO.getDescription());
                movementToUpdate.setStockTransaction(existingTransaction);

                movementRepository.save(movementToUpdate);

                // --- Manage the nested SaleItem ---
                if (movementDTO.getSaleItem() != null) {
                    if (sale == null) {
                        throw new IllegalStateException("Cannot create a SaleItem without a parent Sale.");
                    }

                    Optional<SaleItemEntity> existingSaleItem = saleItemRepository.
                            findActiveByStockMovementId(movementToUpdate.getId());
                    SaleItemEntity saleItemToUpdate;

                    if (existingSaleItem.isPresent()) {
                        saleItemToUpdate = existingSaleItem.get();
                    } else {
                        saleItemToUpdate = new SaleItemEntity();
                        saleItemToUpdate.setSale(sale);
                        saleItemToUpdate.setStockMovement(movementToUpdate);
                        saleItemToUpdate.setStockMovementId(movementToUpdate.getId());
                    }

                    saleItemToUpdate.setPrice(movementDTO.getSaleItem().getPrice());
                    saleItemToUpdate.setDescription(movementDTO.getSaleItem().getDescription());
                    saleItemRepository.save(saleItemToUpdate);

                } else {
                    Optional<SaleItemEntity> existingSaleItem = saleItemRepository
                            .findActiveByStockMovementId(movementToUpdate.getId());
                    existingSaleItem.ifPresent(saleItemRepository::delete);
                }
            }
        }

        for (StockMovementEntity movementToDelete : existingMovementsMap.values()) {
            movementRepository.delete(movementToDelete);

            Optional<SaleItemEntity> existingSaleItem = saleItemRepository
                    .findActiveByStockMovementId(movementToDelete.getId());
            existingSaleItem.ifPresent(saleItemRepository::delete);
        }

        // --- Return the updated transaction DTO ---
        repository.save(existingTransaction);

        return transactionViewService.getActiveTransactionByTransactionId(existingTransaction.getId());
    }

    @Transactional
    public void deleteOperation(Long transactionId) {
        logger.info("Soft-deleting transaction with ID: {}", transactionId);

        // Find the active transaction. If it's not found or already deleted, an exception is thrown.
        repository.findActiveById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Active transaction not found with id: "
                        + transactionId));

        // -- Delete sale and purchase --
        // Find and soft-delete the Sale and SaleItems if they exist
        Optional<SaleEntity> optionalSale = saleRepository.findActiveByStockTransactionId(transactionId);

        if (optionalSale.isPresent()) {
            SaleEntity existingSale = optionalSale.get();

            // Find and soft-delete all related SaleItems
            List<SaleItemEntity> saleItems = saleItemRepository.findBySaleId(existingSale.getStockTransactionId());
            for (SaleItemEntity item : saleItems) {
                saleItemRepository.delete(item); // This will trigger the @SQLDelete on SaleItem
            }

            // Now soft-delete the Sale itself
            saleRepository.delete(existingSale); // This will trigger @SQLDelete on Sale
        }

        // Find and soft-delete the Purchase and PurchaseItems if they exist
        Optional<PurchaseEntity> optionalPurchase = purchaseRepository.findActiveByStockTransactionId(transactionId);

        if (optionalPurchase.isPresent()) {
            PurchaseEntity existingPurchase = optionalPurchase.get();

            // Find and soft-delete all related purchaseItems
            List<PurchaseItemEntity> purchaseItems = purchaseItemRepository.findByPurchaseId(existingPurchase.getStockTransactionId());
            for (PurchaseItemEntity item : purchaseItems) {
                purchaseItemRepository.delete(item); // This will trigger the @SQLDelete on PurchaseItem
            }

            // Now soft-delete the Purchase itself
            purchaseRepository.delete(existingPurchase); // This will trigger @SQLDelete on Purchase
        }
        // -- End delete sale and purchase --

        // Soft-delete all the StockMovements
        List<StockMovementEntity> movements = movementRepository.findActiveByStockTransactionId(transactionId);
        for (StockMovementEntity movement : movements) {
            movementRepository.delete(movement); // This will trigger @SQLDelete on StockMovement
        }

        // Finally, soft-delete the main StockTransaction
        repository.deleteById(transactionId); // This will trigger @SQLDelete on StockTransaction

        logger.info("Transaction with ID: {} and its related records have been soft-deleted.", transactionId);
    }

    // Helper method to build a single DTO from a list of TransactionView entities
    private TransactionOperationResDTO buildTransactionOperationResDTO(List<StockMovementDetailsEntity> entities) {
        if (entities.isEmpty()) {
            return null; // Should not happen with grouping, but for safety
        }

        StockMovementDetailsEntity mainEntity = entities.getFirst();

        // Find transaction type description
        String transactionTypeDescription = getTransactionTypeDescription(mainEntity);

        TransactionOperationResDTO responseDTO = TransactionOperationResDTO.builder()
                .transactionId(mainEntity.getTransactionId())
                .transactionType(TransactionOperationMapper.mapToStockTransactionTypeCompactDTO(mainEntity, transactionTypeDescription))
                .performedBy(TransactionOperationMapper.mapToAppUserCompactDTO(mainEntity))
                .transactionDate(mainEntity.getTransactionDate())
                .description(mainEntity.getTransactionDescription())
                .build();

        // Find sale
        Optional<SaleEntity> findSale = saleRepository.findActiveById(mainEntity.getSaleId());
        SaleEntity sale = null;
        if (findSale.isPresent())
            sale = findSale.get();
        else
            logger.info("This transaction (id:{}) has no sale record", mainEntity.getSaleId());

        ArrayList<StockMovementTOResDTO> stockMovements = new ArrayList<>();
        BigDecimal sumSale = BigDecimal.ZERO;
        for (StockMovementDetailsEntity entity : entities) {
            stockMovements.add(TransactionOperationMapper.mapToStockMovementTOResDTO(entity));
            if (entity.getSaleItemPrice() != null) {
                sumSale = sumSale.add(entity.getSaleItemPrice());
            }
        }
        if (sale != null)
            responseDTO.setSale(processSale(sale, sumSale));
        responseDTO.setStockMovements(stockMovements);

        return responseDTO;
    }

    // Helper
    private TransactionEntities validateAndFetchTransactionEntities(TransactionOperationReqDTO dto) {
        StockTransactionTypeEntity transactionType = transactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction type not found with id: " +
                        dto.getTransactionTypeId()));

        AppUserEntity user = userRepository.findById(dto.getPerformedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
                        dto.getPerformedById()));

        CustomerEntity customer = null;
        if (dto.getSaleRequestDTO() != null && dto.getSaleRequestDTO().getCustomerId() != null) {
            customer = customerRepository.findById(dto.getSaleRequestDTO().getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " +
                            dto.getSaleRequestDTO().getCustomerId()));
        }

        // Not allowed to create a new seed batch for a transaction type: STOCK_OUT or ADJUST with a movement type: ADJUST_OUT
        validateSeedBatchExisting(dto, transactionType.getName());

        // The Sum of quantity of batch is less than the sum of request of stock out movement quantity
        validateStockMovementQuantity(dto, transactionType.getName());

        return new TransactionEntities(transactionType, user, customer);
    }

    /**
     * (Optimized by Gemini)
     * Seed batch must exist for transaction type: STOCK_OUT
     * or transaction type: ADJUSTMENT with movement type's effect on stock is OUT
     * @param dto             contains request data from frontend
     * @param transactionTypeName transaction type entity defined in caller
     */
    private void validateSeedBatchExisting(TransactionOperationReqDTO dto, String transactionTypeName) {
        if (dto.getStockMovements() == null) {
            return; // Nothing to validate
        }

        // If a transaction type is STOCK_OUT or transaction type is ADJUST, and movement effect-on-stock is OUT
        // Throw error if seed batch doesn't exist
        for (StockMovementTOReqDTO movementReqDTO : dto.getStockMovements()) {
            // Validate Movement Type (ResourceNotFound is fine here as it's an ID lookup)
            StockMovementTypeEntity movementTypeEntity = movementTypeRepository.findById(movementReqDTO.getMovementTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movement Type record not found with id: " +
                            movementReqDTO.getMovementTypeId()));

            // Check the condition for requiring a SeedBatch
            boolean requiresBatchCheck = transactionTypeName.equals("STOCK_OUT") ||
                    (transactionTypeName.equals("ADJUSTMENT") && movementTypeEntity.getEffectOnStock() == EffectOnStock.OUT);

            if (requiresBatchCheck) {
                SeedBatchRequestDTO seedBatchReqDTO = movementReqDTO.getSeedBatch();

                // Critical: Check if the SeedBatch object itself is null before accessing its fields
                if (seedBatchReqDTO == null) {
                    throw new BadRequestException("Movement of type " + movementTypeEntity.getName() + " requires Seed Batch details.");
                }

                if (movementReqDTO.getSeedBatch().getId() == null) {
                    // Throw an exception that signals an input validation failure (e.g., 400 Bad Request)
                    String errMsg = String.format("Transaction type '%s' requires an existing Seed Batch, but one or more of batch IDs are null", transactionTypeName);
                    throw new BadRequestException(errMsg);
                }

                // If seed batch exists but not founds in a database
                final Long seedBatchId = movementReqDTO.getSeedBatch().getId();
                batchRepository.findById(seedBatchId)
                        .orElseThrow(() -> new ResourceNotFoundException("Seed Batch record not found with id: " + seedBatchId));
            }
        }
    }

    /**
     * Helper method to validate stock movement quantity for transaction type STOCKOUT or
     * ADJUSTMENT with stock movement type's effect on stock is OUT
     * @param dto holds info of all stock movements sent from frontend
     */
    private void validateStockMovementQuantity(TransactionOperationReqDTO dto, String  transactionTypeName) {
        // -- Filter dto to match criteria --
        // Filter the movements to only include those that require a stock validation (OUT movements)
        ArrayList<StockMovementTOReqDTO> movementsToValidate = dto.getStockMovements().stream()
                .filter(movementReqDTO -> {
                    // Fetch movement type entity for stock effect determination
                    StockMovementTypeEntity movementTypeEntity = movementTypeRepository.findById(movementReqDTO.getMovementTypeId())
                            .orElseThrow(() -> new ResourceNotFoundException("Movement Type record not found with id: " + movementReqDTO.getMovementTypeId()));

                    // Check the exact condition for requiring a SeedBatch check/validation
                    return transactionTypeName.equals("STOCK_OUT") ||
                            (transactionTypeName.equals("ADJUSTMENT") && movementTypeEntity.getEffectOnStock() == EffectOnStock.OUT);
                })
                .collect(Collectors.toCollection(ArrayList::new)); // ⬅️ Changed the terminal operation

        // If no movements require validation, exit early
        if (movementsToValidate.isEmpty()) {
            return;
        }

        // Create a filtered DTO to pass to the helper method
        // NOTE: If you cannot modify the DTO class (e.g., it's generated), you can just pass the List directly,
        // but assuming you need to maintain the original method signature:
        TransactionOperationReqDTO filteredDto = new TransactionOperationReqDTO();
        filteredDto.setStockMovements(movementsToValidate);

        // -- Calculate the sum of request movement quantity by batch id --
        // Get the current, total active stock grouped by seedBatchId from the database
        List<Map<String, Object>> netBatches = transactionDetailRepository.getNetQuantityOfAllActiveStockBySeedBatchId();

        // Convert List<Map> to Map<Long, BigDecimal> for easy lookup
        Map<Long, BigDecimal> currentStock = netBatches.stream()
                .collect(Collectors.toMap(
                        map -> (Long) map.get("seedBatchId"), // Key is seedBatchId
                        map -> (BigDecimal) map.get("netQuantity") // Value is the current total quantity
                ));

        // Calculate the net change for the incoming transaction DTOs
        Map<Long, BigDecimal> requestNetChange = sumQuantitiesBySeedBatchId(filteredDto);

        // -- Compare request of sum of movement quantity by batch id with database's --
        // Compare the request net change against the current stock
        for (Map.Entry<Long, BigDecimal> entry : requestNetChange.entrySet()) {
            Long seedBatchId = entry.getKey();
            BigDecimal netChange = entry.getValue(); // Already signed (+ for IN, - for OUT)

            // Only need to check for movements that result in a net outflow (negative netChange)
            if (netChange.compareTo(BigDecimal.ZERO) < 0) {

                // Get the current stock for this batch (defaults to 0 if batch has no stock yet)
                BigDecimal stockBeforeChange = currentStock.getOrDefault(seedBatchId, BigDecimal.ZERO);

                // Calculate the hypothetical final stock: Stock + (Negative Net Change)
                BigDecimal finalStock = stockBeforeChange.add(netChange);

                if (finalStock.compareTo(BigDecimal.ZERO) < 0) {
                    // The Final stock is less than zero, throw an error
                    String errMsg = String.format(
                            "Insufficient stock for Seed Batch ID %d. Current active stock: %s. Requested net outflow: %s.",
                            seedBatchId,
                            stockBeforeChange.toPlainString(),
                            netChange.abs().toPlainString() // Display as a positive outflow value
                    );

                    throw new BadRequestException(errMsg);
                }
            }
        }
    }

    // Helper: Get sum quantity of a seed batch (By Gemini)
    public Map<Long, BigDecimal> sumQuantitiesBySeedBatchId(TransactionOperationReqDTO dto) {
        // You would typically inject or pass this repository/service
        return dto.getStockMovements().stream()
                // 1. Filter out movements that do not have an ID for the SeedBatch
                // This assumes that if a batch exists, its ID is present in SeedBatchRequestDTO.
                .filter(movement -> movement.getSeedBatch() != null && movement.getSeedBatch().getId() != null)
                // 2. Map to a structure containing the Batch ID (Key) and the Signed Quantity (Value)
                .map(movementReqDTO -> {
                    // Look up the movement type entity to determine the effect on stock
                    StockMovementTypeEntity movementTypeEntity = movementTypeRepository.findById(movementReqDTO.getMovementTypeId())
                            .orElseThrow(() -> new ResourceNotFoundException("Movement Type record not found with id: " + movementReqDTO.getMovementTypeId()));

                    // Determine the sign based on the effect on stock
                    BigDecimal signedQuantity = movementReqDTO.getQuantity().multiply(
                            movementTypeEntity.getEffectOnStock() == EffectOnStock.IN ?
                                    BigDecimal.ONE :
                                    BigDecimal.ONE.negate() // Use -1 for OUT
                    );

                    // Return a Map.Entry: Key = SeedBatch ID, Value = Signed Quantity
                    return Map.entry(
                            movementReqDTO.getSeedBatch().getId(),
                            signedQuantity
                    );
                })
                // 3. Group by the SeedBatch ID and sum the signed quantities
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // The key is the SeedBatch ID
                        Map.Entry::getValue, // The value is the signed quantity
                        BigDecimal::add     // The merge function to sum quantities for the same SeedBatch ID
                ));
    }

    // Helper method to validate and fetch entities for a single movement
    private MovementEntities validateAndFetchMovementEntities(StockMovementTOReqDTO sm) {
        RiceVarietyEntity variety = varietyRepository.findById(sm.getSeedBatch().getVarietyId())
                .orElseThrow(() -> new ResourceNotFoundException("RiceVariety record not found with id: " +
                        sm.getSeedBatch().getVarietyId()));
        SeasonEntity season = seasonRepository.findById(sm.getSeedBatch().getSeasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Season record not found with id: " +
                        sm.getSeedBatch().getSeasonId()));
        RiceGenerationEntity generation = generationRepository.findById(sm.getSeedBatch().getGenerationId())
                .orElseThrow(() -> new ResourceNotFoundException("RiceGeneration record not found with id: " +
                        sm.getSeedBatch().getGenerationId()));
        StockMovementTypeEntity stockMovementType = movementTypeRepository.findById(sm.getMovementTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement type not found with id: " +
                        sm.getMovementTypeId()));

        return new MovementEntities(variety, season, generation, stockMovementType);
    }

    // Helper: This method handles the creation of the Transaction entity.
    private StockTransactionEntity createAndSaveTransaction(TransactionOperationReqDTO dto,
                                                            TransactionEntities entities) {
        StockTransactionEntity stockTransaction = StockTransactionEntity.builder()
                .transactionType(entities.transactionType()) // Use getter from the data class
                .performedBy(entities.user()) // Use getter
                .transactionDate(dto.getTransactionDate())
                .description(dto.getDescription())
                .build();

        StockTransactionEntity savedTransaction = repository.save(stockTransaction);

        if (savedTransaction.getId() != null) {
            logger.info("StockTransaction created successfully with ID: {}", savedTransaction.getId());
            return savedTransaction;
        } else {
            logger.error("Failed to create StockTransaction for dto: {}", dto);
            throw new RuntimeException("Failed to create transaction record.");
        }
    }

    // Helper: This method handles the creation of the Sale entity, which is optional.
    private SaleEntity createAndSaveSale(TransactionOperationReqDTO dto,
                                         StockTransactionEntity savedTransaction,
                                         TransactionEntities transactionEntities) {
        if (dto.getSaleRequestDTO() != null) {
            SaleEntity sale = SaleEntity.builder()
                    .stockTransaction(savedTransaction)
                    .customer(transactionEntities.customer()) // Correctly access via getter
                    .description(dto.getSaleRequestDTO().getDescription())
                    .build();

            SaleEntity savedSale = saleRepository.save(sale);

            if (savedSale.getStockTransactionId() != null) {
                logger.info("Sale created successfully with ID: {}", savedSale.getStockTransactionId());
                return savedSale;
            } else {
                logger.error("Failed to create Sale record");
                throw new RuntimeException("Failed to create Sale record.");
            }
        }
        return null;
    }

    // Helper: get transaction type description
    private String getTransactionTypeDescription(StockMovementDetailsEntity entity) {
        StockTransactionTypeEntity existingTransactionType = transactionTypeRepository.findById(entity.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction type not found with id: "
                        + entity.getTransactionTypeId()));
        return existingTransactionType.getDescription();
    }

    // Generate TOSaleDTO
    private TOSaleResDTO processSale(SaleEntity entity, BigDecimal sumSale) {
        return TOSaleResDTO.builder()
                .customer(transactionMapper.toCustomerCompactDTO(entity.getCustomer()))
                .sumSale(sumSale)
                .build();
    }

    // Should return ArrayList<StockMovementTOResDTO>
    private ArrayList<StockMovementTOResDTO> processStockMovementsForStockIn(TransactionOperationReqDTO dto,
                                                                             StockTransactionEntity savedTransaction,
                                                                             SaleEntity savedSale) {
        if (dto.getStockMovements() == null || dto.getStockMovements().isEmpty()) {
            throw new IllegalArgumentException("Stock movements cannot be empty.");
        }

        ArrayList<StockMovementTOResDTO> stockMovementTOResDTOS = new ArrayList<>();

        int movementCount = 0;
        for (StockMovementTOReqDTO sm : dto.getStockMovements()) {
            logger.info("{}Movement #: {}{}", ANSI_YELLOW, ++movementCount, ANSI_RESET);

            StockMovementTOResDTO stockMovementTOResDTO;

            // Fetch all related entities for the movement at once
            MovementEntities movementEntities = validateAndFetchMovementEntities(sm);

            // Find or create SeedBatch
            SeedBatchEntity savedSeedBatch = findOrCreateSeedBatch(sm, movementEntities);

            // Create and save StockMovement
            StockMovementEntity savedStockMovement = createAndSaveStockMovement(sm,
                    savedTransaction, savedSeedBatch, movementEntities);

            // Create stockMovementTOResDTO
            StockMovementResponseDTO stockMovementResDTO = movementMapper
                    .toResponseDTO(savedStockMovement);
            // Add info
            stockMovementTOResDTO = StockMovementTOResDTO.builder()
                    .id(stockMovementResDTO.getId())
                    .movementType(stockMovementResDTO.getMovementType())
                    .seedBatch(stockMovementResDTO.getSeedBatch())
                    .quantity(stockMovementResDTO.getQuantity())
                    .description(stockMovementResDTO.getDescription())
                    .build();

            // Create and save SaleItem (if applicable)
            if (savedSale != null && sm.getSaleItem() != null) {
                SaleItemEntity savedSaleItem = createAndSaveSaleItem(sm, savedSale, savedStockMovement);

                SaleItemTOResDTO saleItemTOResDTO = SaleItemTOResDTO.builder()
                        .price(savedSaleItem.getPrice())
                        .description(savedSaleItem.getDescription())
                        .build();

                // Update StockMovementTOResDTD with SaleItem
                stockMovementTOResDTO.setSaleItem(saleItemTOResDTO);
            }

            // Add item to stockMovementTOResDTOs
            stockMovementTOResDTOS.add(stockMovementTOResDTO);
        }

        return stockMovementTOResDTOS;
    }

    // This goes inside your TransactionOperationService class
    private SeedBatchEntity findOrCreateSeedBatch(StockMovementTOReqDTO sm, MovementEntities entities) {
        if (sm.getSeedBatch().getId() != null) {
            Long seedBatchId = sm.getSeedBatch().getId();
            SeedBatchEntity existingSeedBatch = batchRepository.findById(seedBatchId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seed Batch record not found with id: " + seedBatchId));
            logger.info("Use specified seed batch with ID: {}",existingSeedBatch.getId());
            return existingSeedBatch;
        }

        Optional<SeedBatchEntity> seedBatch = batchRepository
                .findByVarietyIdAndYearAndSeasonIdAndGradingAndGenerationIdAndGermination(
                        entities.variety().getId(),
                        sm.getSeedBatch().getYear(),
                        entities.season().getId(),
                        sm.getSeedBatch().getGrading(),
                        entities.generation().getId(),
                        sm.getSeedBatch().getGermination()
                );

        if (seedBatch.isPresent()) {
            logger.info("Use existing SeedBatch with ID: {}", seedBatch.get().getId());
            return seedBatch.get();
        } else {
            logger.info("No seed batch for specified data >>> Creating new Seed Batch record...");
            SeedBatchEntity newSeedBatch = SeedBatchEntity.builder()
                    .variety(entities.variety())
                    .generation(entities.generation())
                    .season(entities.season())
                    .year(sm.getSeedBatch().getYear())
                    .grading(sm.getSeedBatch().getGrading())
                    .germination(sm.getSeedBatch().getGermination())
                    .description(sm.getSeedBatch().getDescription())
                    .build();

            SeedBatchEntity savedSeedBatch = batchRepository.save(newSeedBatch);

            if (savedSeedBatch.getId() != null) {
                logger.info("SeedBatch created successfully with ID: {}", savedSeedBatch.getId());
                return savedSeedBatch;
            } else {
                logger.error("Failed to create SeedBatch record");
                throw new RuntimeException("Failed to create SeedBatch record.");
            }
        }
    }

    private StockMovementEntity createAndSaveStockMovement(
            StockMovementTOReqDTO sm,
            StockTransactionEntity savedTransaction,
            SeedBatchEntity savedSeedBatch,
            MovementEntities entities) {

        StockMovementEntity stockMovement = StockMovementEntity.builder()
                .movementType(entities.movementType())
                .seedBatch(savedSeedBatch)
                .stockTransaction(savedTransaction)
                .quantity(sm.getQuantity())
                .description(sm.getDescription())
                .build();

        StockMovementEntity savedStockMovement = movementRepository.save(stockMovement);

        if (savedStockMovement.getId() != null) {
            logger.info("StockMovement created successfully with ID: {}", savedStockMovement.getId());
            return savedStockMovement;
        } else {
            logger.error("Failed to create StockMovement record");
            throw new RuntimeException("Failed to create StockMovement record.");
        }
    }

    // Should return SaleItem
    private SaleItemEntity createAndSaveSaleItem(
            StockMovementTOReqDTO sm,
            SaleEntity savedSale,
            StockMovementEntity savedStockMovement) {

        SaleItemEntity saleItem = SaleItemEntity.builder()
                .sale(savedSale)
                .price(sm.getSaleItem().getPrice())
                .stockMovement(savedStockMovement)
                .description(sm.getSaleItem().getDescription())
                .build();

        SaleItemEntity savedSaleItem = saleItemRepository.save(saleItem);

        if (savedSaleItem.getStockMovementId() != null) {
            logger.info("SaleItem created successfully with ID: {}",
                    savedSaleItem.getStockMovementId());
        } else {
            logger.error("Failed to create SaleItem record");
            throw new RuntimeException("Failed to create SaleItem record.");
        }

        return savedSaleItem;
    }

    // The record classes
    private record TransactionEntities(StockTransactionTypeEntity transactionType,
                                       AppUserEntity user, CustomerEntity customer) {

    }

    private record MovementEntities(RiceVarietyEntity variety, SeasonEntity season,
                                    RiceGenerationEntity generation, StockMovementTypeEntity movementType) {
    }

    //---------------------------------
    // createStockInOperation Helpers
    //---------------------------------

    // -- Helper Methods for createOperation()
    // Create and save transaction
    private StockTransactionEntity createAndSaveTransaction(StockTransactionTypeEntity transactionType, AppUserEntity user, Instant transactionDate, String description) {
        StockTransactionEntity stockTransaction = StockTransactionEntity.builder()
                .transactionType(transactionType) // Use getter from the data class
                .performedBy(user) // Use getter
                .transactionDate(transactionDate)
                .description(description)
                .build();

        StockTransactionEntity savedTransaction = repository.save(stockTransaction);

        if (savedTransaction.getId() != null) {
            logger.info("[createAndSaveTransaction_v2] New stockTransaction created successfully with ID: {}", savedTransaction.getId());
            return savedTransaction;
        } else {
            logger.error("[createAndSaveTransaction_v2] Failed to create StockTransaction record");
            throw new RuntimeException("[createAndSaveTransaction_v2] Failed to create transaction record.");
        }
    }

    private PurchaseEntity createAndSavePurchase(StockTransactionEntity transaction, Integer supplierId){
        // Get supplier
        SupplierEntity supplierEntity;
        if (supplierId == null)
            supplierEntity = null;
        else {
            supplierEntity = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with Id: "+supplierId));
        }

        // Create purchase
        // Note: do not add stockTransactionId(transaction.getId()) because it will be handled by @MapsID in entity else error will occur
        PurchaseEntity purchase = PurchaseEntity.builder()
                .stockTransaction(transaction)
                .supplier(supplierEntity)
                .build();

        // Save purchase
        PurchaseEntity savedPurchase = purchaseRepository.save(purchase);
        if (savedPurchase.getStockTransactionId() != null){
            logger.info("New purchase created successfully with ID: {}", savedPurchase.getStockTransactionId());
            return savedPurchase;
        }else {
            throw new RuntimeException("Failed to create new purchase.");
        }
    }

    // Helper for
    // Create and save movements version 2
    private ArrayList<TOStockMovementStockInResDTO> processStockMovementsForStockIn(
            ArrayList<TOStockMovementStockInReqDTO> movementReqDTOs,
            StockTransactionEntity transaction, PurchaseEntity purchase, OperationTask task
    ){

        if (movementReqDTOs == null || movementReqDTOs.isEmpty()) {
            throw new IllegalArgumentException("[processStockMovements_v2] Stock movements cannot be empty.");
        }

        ArrayList<TOStockMovementStockInResDTO> movementStockInResDTOS = new ArrayList<>();

        // Create movement in loop
        int movementCount = 0;
        for (TOStockMovementStockInReqDTO movementRequestDTO: movementReqDTOs){
            logger.info(ANSI_YELLOW+"[processStockMovements_v2] Movement #{}"+ANSI_RESET, ++movementCount);

            // Get a request movement type
            StockMovementTypeEntity movementType = movementTypeRepository.findById(movementRequestDTO.getMovementTypeId())
                    .orElseThrow(()->new ResourceNotFoundException("[processStockMovements_v2] Movement type not found with id: "+movementRequestDTO.getMovementTypeId()));

            // Check if movement item's movement type is different from a task
            if (OperationTask.valueOf(movementType.getName()) != task){
                throw new BadRequestException("[processStockMovements_v2] transaction task and movement type must be the same");
            }

            // Find or create a seed batch
            SeedBatchEntity savedSeedBatch = findOrCreateSeedBatch(movementRequestDTO.getSeedBatch());

            // Create and save movement
            StockMovementEntity savedMovement = createAndSaveStockMovement(movementRequestDTO, transaction, savedSeedBatch, movementType);

            PurchaseItemEntity savedPurchaseItem = null; // null is for a task type is not purchase

            // Create purchase item if a task is purchase
            if (task == OperationTask.PURCHASE && OperationTask.valueOf(movementType.getName()) == OperationTask.PURCHASE){
                if (movementRequestDTO.getPurchaseItem() == null)
                    throw new BadRequestException("[processStockMovements_v2] Purchase item must be specified for purchase task");

                savedPurchaseItem = createAndSavePurchaseItem(movementRequestDTO.getPurchaseItem(), purchase, savedMovement);
            }

            // Create movement response dto and add to list
            TOStockMovementStockInResDTO movementStockInResDTO = TransactionOperationMapper.mapToTOStockMovementStockInResDTO(
                    savedMovement, movementType, savedSeedBatch, savedPurchaseItem
            );
            movementStockInResDTOS.add(movementStockInResDTO);

        }

        return movementStockInResDTOS;
    }

    // Helper for processStockMovements() v2
    private StockMovementEntity createAndSaveStockMovement(
            TOStockMovementStockInReqDTO movementReqDTO,
            StockTransactionEntity savedTransaction,
            SeedBatchEntity savedSeedBatch,
            StockMovementTypeEntity movementType) {

        StockMovementEntity stockMovement = StockMovementEntity.builder()
                .movementType(movementType)
                .seedBatch(savedSeedBatch)
                .stockTransaction(savedTransaction)
                .quantity(movementReqDTO.getQuantity())
                .description(movementReqDTO.getDescription())
                .build();

        StockMovementEntity savedStockMovement = movementRepository.save(stockMovement);

        if (savedStockMovement.getId() != null) {
            logger.info("[createAndSaveStockMovement_v2] StockMovement created successfully with ID: {}", savedStockMovement.getId());
            return savedStockMovement;
        } else {
            logger.error("[createAndSaveStockMovement_v2] Failed to create StockMovement record");
            throw new RuntimeException("Failed to create StockMovement record.");
        }
    }

    // Helper method for processStockMovements()
    private SeedBatchEntity findOrCreateSeedBatch(SeedBatchRequestDTO batchRequestDTO) {
        if (batchRequestDTO.getId() != null) {
            Long seedBatchId = batchRequestDTO.getId();
            SeedBatchEntity existingSeedBatch = batchRepository.findById(seedBatchId)
                    .orElseThrow(() -> new ResourceNotFoundException("[findOrCreateSeedBatch_v2] Seed Batch record not found with id: " + seedBatchId));
            logger.info("[findOrCreateSeedBatch_v2] Use specified seed batch with ID: {}",existingSeedBatch.getId());
            return existingSeedBatch;
        }

        Optional<SeedBatchEntity> seedBatch = batchRepository
                .findByVarietyIdAndYearAndSeasonIdAndGradingAndGenerationIdAndGermination(
                        batchRequestDTO.getVarietyId(),
                        batchRequestDTO.getYear(),
                        batchRequestDTO.getSeasonId(),
                        batchRequestDTO.getGrading(),
                        batchRequestDTO.getGenerationId(),
                        batchRequestDTO.getGermination()
                );

        if (seedBatch.isPresent()) {
            logger.info("[findOrCreateSeedBatch_v2] Use existing SeedBatch with ID: {}", seedBatch.get().getId());
            return seedBatch.get();
        } else {
            logger.info("[findOrCreateSeedBatch_v2] No seed batch for specified data >>> Creating new Seed Batch record...");
            SeedBatchChildrenEntities childrenEntities = getSeedBatchChildrenEntities(batchRequestDTO);
            SeedBatchEntity newSeedBatch = SeedBatchEntity.builder()
                    .variety(childrenEntities.variety())
                    .generation(childrenEntities.generation())
                    .season(childrenEntities.season())
                    .year(batchRequestDTO.getYear())
                    .grading(batchRequestDTO.getGrading())
                    .germination(batchRequestDTO.getGermination())
                    .description(batchRequestDTO.getDescription())
                    .build();

            SeedBatchEntity savedSeedBatch = batchRepository.save(newSeedBatch);

            if (savedSeedBatch.getId() != null) {
                logger.info("[findOrCreateSeedBatch_v2] SeedBatch created successfully with ID: {}", savedSeedBatch.getId());
                return savedSeedBatch;
            } else {
                logger.error("[findOrCreateSeedBatch_v2] Failed to create SeedBatch record");
                throw new RuntimeException("Failed to create SeedBatch record.");
            }
        }
    }

    // Helper for findOrCreateSeedBatch() to get entities in seed batch: variety, generation, season
    private SeedBatchChildrenEntities getSeedBatchChildrenEntities(SeedBatchRequestDTO dto){
        RiceVarietyEntity variety = varietyRepository.findById(dto.getVarietyId())
                .orElseThrow(() -> new ResourceNotFoundException("RiceVariety record not found with id: " +
                        dto.getVarietyId()));
        SeasonEntity season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Season record not found with id: " +
                        dto.getSeasonId()));
        RiceGenerationEntity generation = generationRepository.findById(dto.getGenerationId())
                .orElseThrow(() -> new ResourceNotFoundException("RiceGeneration record not found with id: " +
                        dto.getGenerationId()));

        return new SeedBatchChildrenEntities(variety, generation, season);
    }

    // Helper for processStockMovements()
    private PurchaseItemEntity createAndSavePurchaseItem(
            PurchaseItemRequestDTO purchaseItemRequestDTO,
            PurchaseEntity purchase,
            StockMovementEntity movement
    ){
        // Note // Note: do not add stockMovementId(movement.getId()) because it will be handled by @MapsID in entity else error will occur
        PurchaseItemEntity purchaseItem = PurchaseItemEntity.builder()
                .stockMovement(movement)
                .purchase(purchase)
                .price(purchaseItemRequestDTO.getPrice())
                .description(purchaseItemRequestDTO.getDescription())
                .build();

        PurchaseItemEntity savedPurchaseItem = purchaseItemRepository.save(purchaseItem);

        if (savedPurchaseItem.getStockMovementId() != null) {
            logger.info("Purchase item created successfully with ID: {}",
                    savedPurchaseItem.getStockMovementId());
            return savedPurchaseItem;
        } else {
            throw new RuntimeException("Failed to create purchase item.");
        }
    }

    // Record class
    private record SeedBatchChildrenEntities(RiceVarietyEntity variety, RiceGenerationEntity generation, SeasonEntity season){}

    //--------------------------------------
    // createStockInOperation Helpers (End)
    //--------------------------------------
}
