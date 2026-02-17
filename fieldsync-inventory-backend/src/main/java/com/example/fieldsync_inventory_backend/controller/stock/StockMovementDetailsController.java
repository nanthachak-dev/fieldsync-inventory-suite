package com.example.fieldsync_inventory_backend.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.PagedResponse;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.RiceVarietySaleResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.RiceVarietyStockResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.StockMovementDetailsResponseDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.SyncRecordDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_details.TotalStockResponseDTO;
import com.example.fieldsync_inventory_backend.service.stock.StockMovementDetailsService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movement-details")
@RequiredArgsConstructor
public class StockMovementDetailsController {
    private final StockMovementDetailsService service;

    @GetMapping
    public ResponseEntity<List<StockMovementDetailsResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStockMovementDetails());
    }

    @GetMapping("/sync")
    // Controller now returns a list of the wrapper DTOs
    public ResponseEntity<List<SyncRecordDTO>> getForSync(@RequestParam("lastSyncTime") Instant lastSyncTime) {

        List<SyncRecordDTO> recordsToSync = service.getUpdatedAndDeletedSince(lastSyncTime);

        return ResponseEntity.ok(recordsToSync);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<StockMovementDetailsResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStockMovementDetails(id));
    }

    @GetMapping("/total-stock")
    public ResponseEntity<TotalStockResponseDTO> getTotalStock(
            @RequestParam(value = "lastDate", required = false) Instant lastDate) {
        return ResponseEntity.ok(service.getTotalStock(lastDate));
    }

    @GetMapping("/rice-variety-stock")
    public ResponseEntity<PagedResponse<RiceVarietyStockResponseDTO>> getRiceVarietyStock(
            @RequestParam(value = "lastDate", required = false) Instant lastDate,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(service.getRiceVarietyStock(lastDate, pageable));
    }

    @GetMapping("/top-selling-varieties")
    public ResponseEntity<PagedResponse<RiceVarietySaleResponseDTO>> getTopSellingVarieties(
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(service.getTopSellingVarieties(startDate, endDate, pageable));
    }
}
