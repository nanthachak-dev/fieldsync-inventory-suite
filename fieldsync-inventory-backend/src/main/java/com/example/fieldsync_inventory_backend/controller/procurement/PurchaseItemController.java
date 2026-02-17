package com.example.fieldsync_inventory_backend.controller.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase_item.PurchaseItemRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.purchase_item.PurchaseItemResponseDTO;
import com.example.fieldsync_inventory_backend.service.procurement.PurchaseItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-items")
@RequiredArgsConstructor
public class PurchaseItemController {
    private final PurchaseItemService service;

    @PostMapping
    public ResponseEntity<PurchaseItemResponseDTO> create(@RequestBody PurchaseItemRequestDTO dto) {
        return ResponseEntity.ok(service.createOrUpdatePurchaseItem(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseItemResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPurchaseItemById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseItemResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllPurchaseItems());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<PurchaseItemResponseDTO>> getAllDeleted() {
        return ResponseEntity.ok(service.getAllSoftDeletedPurchaseItems());
    }

    @GetMapping("/all")
    public ResponseEntity<List<PurchaseItemResponseDTO>> getAllWithDeleted() {
        return ResponseEntity.ok(service.getAllWithDeleted());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePurchaseItem(id);
        return ResponseEntity.noContent().build();
    }
}
