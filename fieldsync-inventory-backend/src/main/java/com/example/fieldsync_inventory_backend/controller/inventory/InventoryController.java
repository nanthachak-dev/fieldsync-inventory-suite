package com.example.fieldsync_inventory_backend.controller.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryBatchResponseDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.InventoryResponseDTO;
import com.example.fieldsync_inventory_backend.service.inventory.InventoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<InventoryResponseDTO> getInventory(
            @RequestParam(value = "lastDate", required = false) Instant lastDate,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getInventory(lastDate, pageable));
    }

    @GetMapping("/batch/{id}")
    public ResponseEntity<InventoryBatchResponseDTO> getInventoryBatch(
            @PathVariable("id") Integer riceVarietyId,
            @RequestParam(value = "lastDate", required = false) Instant lastDate,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getInventoryBatch(riceVarietyId, lastDate, pageable));
    }
}
