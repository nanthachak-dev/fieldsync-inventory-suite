package com.example.fieldsync_inventory_backend.repository.stock;

import com.example.fieldsync_inventory_backend.dto.inventory.InventoryBatchDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventorySummaryDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryVarietyResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.RiceVarietySaleResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.RiceVarietyStockResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.summary.StockSummaryResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.summary.StockVarietySummaryResponseDTO;
import com.example.fieldsync_inventory_backend.entity.view.StockMovementDetailsEntity;
import com.example.fieldsync_inventory_backend.entity.view.StockMovementDetailsId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Make sure your repository has the necessary read-only methods to support the
 * service.
 * This is the correct design for a view repository.*
 */
@org.springframework.stereotype.Repository
public interface StockMovementDetailsRepository extends Repository<StockMovementDetailsEntity, StockMovementDetailsId> {

    Optional<StockMovementDetailsEntity> findById(StockMovementDetailsId id);

    List<StockMovementDetailsEntity> findAll();

    List<StockMovementDetailsEntity> findByTransactionId(Long transactionId);

    // Custom method to find all active transactions by checking the deletedAt
    // column
    @Query("SELECT t FROM StockMovementDetailsEntity t WHERE t.deletedAt IS NULL")
    List<StockMovementDetailsEntity> findAllActive();

    /**
     * Finds all StockMovementDetailsEntity records that were created or updated
     * after the specified 'lastSyncTime'.
     *
     * @param lastSyncTime The Instant timestamp representing the last successful
     *                     sync time.
     * @return A list of entities that need to be synced (new or modified).
     */
    List<StockMovementDetailsEntity> findByCreatedAtAfterOrUpdatedAtAfter(Instant lastSyncTime, Instant lastSyncTime2);

    // Check for NOT NULL to ensure it was actually deleted, and then check the
    // time.
    List<StockMovementDetailsEntity> findByDeletedAtIsNotNullAndDeletedAtAfter(Instant lastSyncTime);

    /**
     * Calculates the net quantity for each Seed Batch ID across ALL active
     * (non-deleted) movements.
     */
    @Query(value = """
                SELECT\s
                    t.seedBatchId AS seedBatchId,
                    SUM(CASE\s
                        WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity\s
                        WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity\s
                        ELSE 0\s
                    END) AS netQuantity
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.seedBatchId IS NOT NULL
                GROUP BY\s
                    t.seedBatchId
            """)
    List<Map<String, Object>> getNetQuantityOfAllActiveStockBySeedBatchId();

    @Query(value = """
                SELECT\s
                    SUM(CASE\s
                        WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity\s
                        WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity\s
                        ELSE 0\s
                    END)
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.transactionDate <= :lastDate
            """)
    BigDecimal getTotalStockBeforeDate(Instant lastDate);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.stock.movement_details.RiceVarietyStockResponseDTO(
                    t.riceVarietyId, t.riceVarietyName, t.riceVarietyImageUrl,
                    SUM(CASE\s
                        WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity\s
                        WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity\s
                        ELSE 0\s
                    END))
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.transactionDate <= :lastDate
                GROUP BY\s
                    t.riceVarietyId, t.riceVarietyName, t.riceVarietyImageUrl
                ORDER BY\s
                    SUM(CASE\s
                        WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity\s
                        WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity\s
                        ELSE 0\s
                    END) DESC
            """)
    Page<RiceVarietyStockResponseDTO> findRiceVarietyStockBeforeDate(Instant lastDate, Pageable pageable);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.stock.movement_details.RiceVarietySaleResponseDTO(
                    t.riceVarietyId, t.riceVarietyName, t.riceVarietyImageUrl,
                    SUM(t.stockMovementQuantity))
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.movementTypeName = 'SALE'
                    AND t.transactionDate BETWEEN :startDate AND :endDate
                GROUP BY\s
                    t.riceVarietyId, t.riceVarietyName, t.riceVarietyImageUrl
                ORDER BY\s
                    SUM(t.stockMovementQuantity) DESC
            """)
    Page<RiceVarietySaleResponseDTO> findTopSellingVarietiesByDateRange(
            Instant startDate, Instant endDate, Pageable pageable);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.inventory.InventoryVarietyResponseDTO(
                    t.riceVarietyId, t.riceVarietyName,
                    SUM(CASE WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END)
                )
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.transactionDate <= :lastDate
                GROUP BY\s
                    t.riceVarietyId, t.riceVarietyName
            """)
    Page<InventoryVarietyResponseDTO> findInventoryVarietyStockBeforeDate(
            Instant lastDate, Pageable pageable);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.inventory.InventorySummaryDTO(
                    SUM(CASE WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END)
                )
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.transactionDate <= :lastDate
            """)
    InventorySummaryDTO getInventorySummaryBeforeDate(Instant lastDate);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.inventory.InventorySummaryDTO(
                    SUM(CASE WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END),
                    SUM(CASE WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END)
                )
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.riceVarietyId = :riceVarietyId
                    AND t.transactionDate <= :lastDate
            """)
    InventorySummaryDTO getInventorySummaryByVarietyIdBeforeDate(
            Integer riceVarietyId, java.time.Instant lastDate);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.inventory.InventoryBatchDTO(
                    t.seedBatchId, t.riceVarietyId, t.riceVarietyName, t.seedBatchYear,
                    t.seasonId, t.seasonName, t.seasonDescription,
                    t.generationId, t.generationName,
                    t.seedBatchGrading, t.seedBatchGermination,
                    SUM(CASE WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END)
                )
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.riceVarietyId = :riceVarietyId
                    AND t.transactionDate <= :lastDate
                GROUP BY\s
                    t.seedBatchId, t.riceVarietyId, t.riceVarietyName, t.seedBatchYear,
                    t.seasonId, t.seasonName, t.seasonDescription,
                    t.generationId, t.generationName,
                    t.seedBatchGrading, t.seedBatchGermination
            """)
    Page<InventoryBatchDTO> findInventoryBatchStockByVarietyIdBeforeDate(
            Integer riceVarietyId, java.time.Instant lastDate, Pageable pageable);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.stock.summary.StockSummaryResponseDTO(
                    COALESCE(SUM(CASE WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0)
                )
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.transactionDate <= :lastDate
            """)
    StockSummaryResponseDTO getStockSummaryBeforeDate(
            Instant lastDate);

    @Query(value = """
                SELECT new com.example.fieldsync_inventory_backend.dto.stock.summary.StockVarietySummaryResponseDTO(
                    t.riceVarietyId, t.riceVarietyName,
                    COALESCE(SUM(CASE WHEN t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGrading = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGrading = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = true AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),

                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.generationName = 'R1' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.generationName = 'R2' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN t.seedBatchGermination = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'IN' THEN t.stockMovementQuantity WHEN t.seedBatchGermination = false AND t.generationName = 'R3' AND t.movementTypeEffectOnStock = 'OUT' THEN -t.stockMovementQuantity ELSE 0 END), 0)
                )
                FROM\s
                    StockMovementDetailsEntity t
                WHERE\s
                    t.deletedAt IS NULL\s
                    AND t.transactionDate <= :lastDate
                GROUP BY\s
                    t.riceVarietyId, t.riceVarietyName
            """)
    Page<StockVarietySummaryResponseDTO> getStockVarietySummaryBeforeDate(
            Instant lastDate, Pageable pageable);
}
