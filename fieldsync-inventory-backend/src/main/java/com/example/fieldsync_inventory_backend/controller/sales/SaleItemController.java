package com.example.fieldsync_inventory_backend.controller.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.sale_item.SaleItemRequestDTO;
import com.example.fieldsync_inventory_backend.dto.sales.sale_item.SaleItemResponseDTO;
import com.example.fieldsync_inventory_backend.service.sales.SaleItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-items")
@RequiredArgsConstructor
public class SaleItemController {
    private final SaleItemService service;

    @PostMapping
    public ResponseEntity<SaleItemResponseDTO> create(@RequestBody SaleItemRequestDTO dto) {
        return ResponseEntity.ok(service.createOrUpdateSaleItem(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleItemResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSaleItemById(id));
    }

    @GetMapping
    public ResponseEntity<List<SaleItemResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSaleItems());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<SaleItemResponseDTO>> getAllDeleted() {
        return ResponseEntity.ok(service.getAllSoftDeletedSaleItems());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SaleItemResponseDTO>> getAllWithDeleted() {
        return ResponseEntity.ok(service.getAllWithDeleted());
    }

    // PUT operation is not utilized because it's one-to-one with StockMovement

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSaleItem(id);
        return ResponseEntity.noContent().build();
    }
}