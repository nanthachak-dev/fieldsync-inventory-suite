package com.example.fieldsync_inventory_backend.controller.stock;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.TransactionOperationReqDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.TransactionOperationResDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.stock_in.TransactionOperationStockInReqDTO;
import com.example.fieldsync_inventory_backend.dto.stock.transaction_operation.stock_in.TransactionOperationStockInResDTO;
import com.example.fieldsync_inventory_backend.service.stock.TransactionOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-operations")
@RequiredArgsConstructor
public class TransactionOperationController {
    private final TransactionOperationService service;

    @PostMapping
    public ResponseEntity<TransactionOperationResDTO> create(
            @RequestBody TransactionOperationReqDTO dto
    ) {
        return ResponseEntity.ok(service.createOperation(dto));
    }

    // Create for Stock In
    @PostMapping("/stock-in")
    public ResponseEntity<TransactionOperationStockInResDTO> createStockIn(
            @RequestBody TransactionOperationStockInReqDTO dto
    ) {
        return ResponseEntity.ok(service.createStockInOperation(dto));
    }

    // The method is disabled because it's not good practice
//    @PutMapping("/{id}")
//    public ResponseEntity<TransactionOperationResDTO> update(
//            @PathVariable Long id, @RequestBody TransactionOperationReqDTO dto) {
//        return ResponseEntity.ok(service.updateOperation(id, dto));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionOperationResDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionOperationResDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<TransactionOperationReqDTO>> getAllWithDeleted() {
//        return ResponseEntity.ok(service.getAllWithDeleted());
//    }

//    @GetMapping("/deleted")
//    public ResponseEntity<List<TransactionOperationReqDTO>> getAllDeleted() {
//        return ResponseEntity.ok(service.getAllSoftDeletedStockMovements());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }
}
