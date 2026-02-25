package com.example.fieldsync_inventory_api.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.stock.transaction_type.StockTransactionTypeRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction_type.StockTransactionTypeResponseDTO;
import com.example.fieldsync_inventory_api.service.stock.StockTransactionTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transaction-types")
@RequiredArgsConstructor
public class StockTransactionTypeController {
    private final StockTransactionTypeService service;

    @PostMapping
    public ResponseEntity<StockTransactionTypeResponseDTO> create(@RequestBody StockTransactionTypeRequestDTO dto) {
        return ResponseEntity.ok(service.createStockTransactionType(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockTransactionTypeResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getStockTransactionTypeById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockTransactionTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStockTransactionTypes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockTransactionTypeResponseDTO> update(@PathVariable Integer id,
                                                          @RequestBody StockTransactionTypeRequestDTO dto) {
        return ResponseEntity.ok(service.updateStockTransactionType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteStockTransactionType(id);
        return ResponseEntity.noContent().build();
    }
}
