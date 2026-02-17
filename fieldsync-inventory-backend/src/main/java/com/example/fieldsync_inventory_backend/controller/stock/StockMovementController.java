package com.example.fieldsync_inventory_backend.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.stock.movement.StockMovementRequestDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement.StockMovementResponseDTO;
import com.example.fieldsync_inventory_backend.service.stock.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {
    private final StockMovementService service;

    @PostMapping
    public ResponseEntity<StockMovementResponseDTO> create(@RequestBody StockMovementRequestDTO dto) {
        return ResponseEntity.ok(service.createStockMovement(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> update(@PathVariable Long id,
                                                           @RequestBody StockMovementRequestDTO dto) {
        return ResponseEntity.ok(service.updateStockMovement(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStockMovementById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockMovementResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStockMovements());
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockMovementResponseDTO>> getAllWithDeleted() {
        return ResponseEntity.ok(service.getAllWithDeleted());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<StockMovementResponseDTO>> getAllDeleted() {
        return ResponseEntity.ok(service.getAllSoftDeletedStockMovements());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStockMovement(id);
        return ResponseEntity.noContent().build();
    }
}
