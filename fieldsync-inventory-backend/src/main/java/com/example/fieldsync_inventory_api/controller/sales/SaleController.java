package com.example.fieldsync_inventory_api.controller.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.sales.sale.SaleRequestDTO;
import com.example.fieldsync_inventory_api.dto.sales.sale.SaleResponseDTO;
import com.example.fieldsync_inventory_api.service.sales.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService service;

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO dto) {
        return ResponseEntity.ok(service.createOrUpdateSale(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSaleById(id));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSales());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<SaleResponseDTO>> getAllDeleted() {
        return ResponseEntity.ok(service.getAllSoftDeletedSales());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SaleResponseDTO>> getAllWithDeleted() {
        return ResponseEntity.ok(service.getAllWithDeleted());
    }

    // PUT operation is not utilized because it's one-to-one to Transaction

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}