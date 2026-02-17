package com.example.fieldsync_inventory_backend.service.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryBatchDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryBatchResponseDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryResponseDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventorySummaryDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryVarietyResponseDTO;
import com.example.fieldsync_inventory_backend.repository.stock.StockMovementDetailsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InventoryService {

        private final StockMovementDetailsRepository repository;

        public InventoryResponseDTO getInventory(Instant lastDate, Pageable pageable) {
                if (lastDate == null) {
                        lastDate = Instant.now();
                }

                InventorySummaryDTO summary = repository.getInventorySummaryBeforeDate(lastDate);
                Page<InventoryVarietyResponseDTO> varietyPage = repository.findInventoryVarietyStockBeforeDate(lastDate,
                                pageable);

                if (summary == null) {
                        summary = new InventorySummaryDTO(java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO,
                                        java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO,
                                        java.math.BigDecimal.ZERO);
                }

                return InventoryResponseDTO.builder()
                                .totalStock(summary.getTotalStock() != null ? summary.getTotalStock()
                                                : java.math.BigDecimal.ZERO)
                                .totalGraded(summary.getTotalGraded() != null ? summary.getTotalGraded()
                                                : java.math.BigDecimal.ZERO)
                                .totalUngraded(
                                                summary.getTotalUngraded() != null ? summary.getTotalUngraded()
                                                                : java.math.BigDecimal.ZERO)
                                .totalGerminated(
                                                summary.getTotalGerminated() != null ? summary.getTotalGerminated()
                                                                : java.math.BigDecimal.ZERO)
                                .totalUngerminated(
                                                summary.getTotalUngerminated() != null ? summary.getTotalUngerminated()
                                                                : java.math.BigDecimal.ZERO)
                                .varieties(varietyPage.getContent())
                                .build();
        }

        public InventoryBatchResponseDTO getInventoryBatch(Integer riceVarietyId, java.time.Instant lastDate,
                        Pageable pageable) {
                if (lastDate == null) {
                        lastDate = java.time.Instant.now();
                }

                InventorySummaryDTO summary = repository.getInventorySummaryByVarietyIdBeforeDate(riceVarietyId,
                                lastDate);
                Page<InventoryBatchDTO> batchPage = repository.findInventoryBatchStockByVarietyIdBeforeDate(
                                riceVarietyId,
                                lastDate, pageable);

                if (summary == null) {
                        summary = new InventorySummaryDTO(java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO,
                                        java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO,
                                        java.math.BigDecimal.ZERO);
                }

                return InventoryBatchResponseDTO.builder()
                                .totalStock(summary.getTotalStock() != null ? summary.getTotalStock()
                                                : java.math.BigDecimal.ZERO)
                                .totalGraded(summary.getTotalGraded() != null ? summary.getTotalGraded()
                                                : java.math.BigDecimal.ZERO)
                                .totalUngraded(
                                                summary.getTotalUngraded() != null ? summary.getTotalUngraded()
                                                                : java.math.BigDecimal.ZERO)
                                .totalGerminated(
                                                summary.getTotalGerminated() != null ? summary.getTotalGerminated()
                                                                : java.math.BigDecimal.ZERO)
                                .totalUngerminated(
                                                summary.getTotalUngerminated() != null ? summary.getTotalUngerminated()
                                                                : java.math.BigDecimal.ZERO)
                                .batches(batchPage.getContent())
                                .build();
        }
}
