package com.example.fieldsync_inventory_api.controller.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.procurement.purchase.PurchaseRequestDTO;
import com.example.fieldsync_inventory_api.dto.procurement.purchase.PurchaseResponseDTO;
import com.example.fieldsync_inventory_api.service.procurement.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService service;

    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> create(@RequestBody PurchaseRequestDTO dto) {
        return ResponseEntity.ok(service.createOrUpdatePurchase(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPurchaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllPurchases());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<PurchaseResponseDTO>> getAllDeleted() {
        return ResponseEntity.ok(service.getAllSoftDeletedPurchases());
    }

    @GetMapping("/all")
    public ResponseEntity<List<PurchaseResponseDTO>> getAllWithDeleted() {
        return ResponseEntity.ok(service.getAllWithDeleted());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
