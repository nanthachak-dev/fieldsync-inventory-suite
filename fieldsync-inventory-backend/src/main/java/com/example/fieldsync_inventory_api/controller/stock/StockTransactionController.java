package com.example.fieldsync_inventory_api.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionRequestDTO;
import com.example.fieldsync_inventory_api.dto.stock.transaction.StockTransactionResponseDTO;
import com.example.fieldsync_inventory_api.service.stock.StockTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transactions")
@RequiredArgsConstructor
public class StockTransactionController {
    private final StockTransactionService service;

    @PostMapping
    public ResponseEntity<StockTransactionResponseDTO> create(@RequestBody StockTransactionRequestDTO dto) {
        return ResponseEntity.ok(service.createStockTransaction(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockTransactionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStockTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockTransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStockTransactions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockTransactionResponseDTO> update(@PathVariable Long id,
                                                           @RequestBody StockTransactionRequestDTO dto) {
        return ResponseEntity.ok(service.updateStockTransaction(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStockTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<StockTransactionResponseDTO>> getAllDeleted() {
        return ResponseEntity.ok(service.getAllSoftDeletedStockTransactions());
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockTransactionResponseDTO>> getAllWithDeleted() {
        return ResponseEntity.ok(service.getAllWithDeleted());
    }
}
