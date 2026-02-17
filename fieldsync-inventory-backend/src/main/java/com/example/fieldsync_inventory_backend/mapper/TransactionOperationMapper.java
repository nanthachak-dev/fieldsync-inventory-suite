package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.user.user.AppUserCompactDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.rice_generation.RiceGenerationCompactDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.rice_variety.RiceVarietyCompactDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.season.SeasonCompactDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_type.StockMovementTypeCompactDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.SaleItemTOResDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.StockMovementTOResDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.TransactionOperationResDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.stock_in.TOStockMovementStockInResDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_type.StockTransactionTypeCompactDTO;
import com.example.fieldsync_inventory_backend.entity.inventory.SeedBatchEntity;
import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseItemEntity;
import com.example.fieldsync_inventory_backend.entity.stock.StockMovementEntity;
import com.example.fieldsync_inventory_backend.entity.stock.StockMovementTypeEntity;
import com.example.fieldsync_inventory_backend.entity.view.StockMovementDetailsEntity;
import com.example.fieldsync_inventory_backend.enums.EffectOnStock;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TransactionOperationMapper {

    public static TransactionOperationResDTO mapToResponseDTO(StockMovementDetailsEntity entity, String transactionTypeDescription) {
        if (entity == null) {
            return null;
        }

        ArrayList<StockMovementTOResDTO> stockMovementsList = new ArrayList<>();
        stockMovementsList.add(mapToStockMovementTOResDTO(entity));

        return TransactionOperationResDTO.builder()
                .transactionId(entity.getTransactionId())
                .transactionType(mapToStockTransactionTypeCompactDTO(entity, transactionTypeDescription))
                .performedBy(mapToAppUserCompactDTO(entity))
                .stockMovements(stockMovementsList)
                .transactionDate(entity.getTransactionDate())
                .description(entity.getTransactionDescription())
                .build();
    }

    public static StockTransactionTypeCompactDTO mapToStockTransactionTypeCompactDTO(StockMovementDetailsEntity entity, String transactionTypeDescription) {
        return StockTransactionTypeCompactDTO.builder()
                .id(entity.getTransactionTypeId())
                .name(entity.getTransactionTypeName())
                .description(transactionTypeDescription)
                .build();
    }

    public static AppUserCompactDTO mapToAppUserCompactDTO(StockMovementDetailsEntity entity) {
        return AppUserCompactDTO.builder()
                .id(entity.getUserId())
                .username(entity.getUsername())
                .build();
    }

    public static StockMovementTOResDTO mapToStockMovementTOResDTO(StockMovementDetailsEntity entity) {
        return StockMovementTOResDTO.builder()
                .id(entity.getStockMovementId())
                .movementType(mapToStockMovementTypeCompactDTO(entity))
                .seedBatch(mapToSeedBatchCompactDTO(entity))
                .saleItem(mapToSaleItemTOResDTO(entity))
                .quantity(entity.getStockMovementQuantity())
                .description(entity.getStockMovementDescription())
                .build();
    }

    public static StockMovementTypeCompactDTO mapToStockMovementTypeCompactDTO(StockMovementDetailsEntity entity) {
        return StockMovementTypeCompactDTO.builder()
                .id(entity.getMovementTypeId())
                .name(entity.getMovementTypeName())
                .effectOnStock(EffectOnStock.valueOf(entity.getMovementTypeEffectOnStock()))
                .description(entity.getMovementTypeDescription())
                .build();
    }

    public static SeedBatchCompactDTO mapToSeedBatchCompactDTO(StockMovementDetailsEntity entity) {
        return SeedBatchCompactDTO.builder()
                .id(entity.getSeedBatchId() != null ? entity.getSeedBatchId() : null)
                .variety(mapToRiceVarietyCompactDTO(entity))
                .generation(mapToRiceGenerationCompactDTO(entity))
                .season(mapToSeasonCompactDTO(entity))
                .year(entity.getSeedBatchYear())
                .grading(entity.getSeedBatchGrading())
                .germination(entity.getSeedBatchGermination())
                .description(entity.getSeedBatchDescription())
                .build();
    }

    // Additional helper mappers for SeedBatchCompactDTO fields
    public static RiceVarietyCompactDTO mapToRiceVarietyCompactDTO(StockMovementDetailsEntity entity) {
        return RiceVarietyCompactDTO.builder()
                .id(entity.getRiceVarietyId())
                .name(entity.getRiceVarietyName())
                .description(entity.getRiceVarietyDescription())
                .imageUrl(entity.getRiceVarietyImageUrl())
                .build();
    }

    public static RiceGenerationCompactDTO mapToRiceGenerationCompactDTO(StockMovementDetailsEntity entity) {
        return RiceGenerationCompactDTO.builder()
                .id(entity.getGenerationId())
                .name(entity.getGenerationName())
                .description(entity.getGenerationDescription())
                .build();
    }

    public static SeasonCompactDTO mapToSeasonCompactDTO(StockMovementDetailsEntity entity) {
        return SeasonCompactDTO.builder()
                .id(entity.getSeasonId())
                .name(entity.getSeasonName())
                .description(entity.getSeasonDescription())
                .build();
    }

    public static SaleItemTOResDTO mapToSaleItemTOResDTO(StockMovementDetailsEntity entity) {
        if (entity.getSaleId() == null) {
            return null;
        }
        return SaleItemTOResDTO.builder()
                .price(entity.getSaleItemPrice())
                .description(entity.getSaleItemDescription())
                .build();
    }

    public static TOStockMovementStockInResDTO mapToTOStockMovementStockInResDTO(
            StockMovementEntity movement,
            StockMovementTypeEntity movementType,
            SeedBatchEntity seedBatch,
            PurchaseItemEntity purchaseItem
    ) {
        BigDecimal price = null;
        if (purchaseItem!=null)
            price = purchaseItem.getPrice();

        return TOStockMovementStockInResDTO.builder()
                .id(movement.getId())
                .movementType(movementType.getName())
                .seedBatchId(seedBatch.getId())
                .purchaseItem(price)
                .quantity(movement.getQuantity())
                .description(movement.getDescription())
                .build();
    }
}
