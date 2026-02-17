package com.example.fieldsync_inventory_backend.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.stock.movement_type.StockMovementTypeRequestDTO;
import com.example.fieldsync_inventory_backend.dto.stock.movement_type.StockMovementTypeResponseDTO;
import com.example.fieldsync_inventory_backend.service.stock.StockMovementTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movement-types")
@RequiredArgsConstructor
public class StockMovementTypeController {

    private final StockMovementTypeService service;

    @PostMapping
    public ResponseEntity<StockMovementTypeResponseDTO> create(@RequestBody StockMovementTypeRequestDTO dto) {
        return ResponseEntity.ok(service.createStockMovementType(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementTypeResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getStockMovementTypeById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockMovementTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStockMovementTypes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovementTypeResponseDTO> update(@PathVariable Integer id,
                                                          @RequestBody StockMovementTypeRequestDTO dto) {
        return ResponseEntity.ok(service.updateStockMovementType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteStockMovementType(id);
        return ResponseEntity.noContent().build();
    }
}
