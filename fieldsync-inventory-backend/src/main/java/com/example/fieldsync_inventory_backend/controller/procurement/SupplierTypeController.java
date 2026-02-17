package com.example.fieldsync_inventory_backend.controller.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier_type.SupplierTypeRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier_type.SupplierTypeResponseDTO;
import com.example.fieldsync_inventory_backend.service.procurement.SupplierTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-types")
@RequiredArgsConstructor
public class SupplierTypeController {
    private final SupplierTypeService service;

    @PostMapping
    public ResponseEntity<SupplierTypeResponseDTO> create(@RequestBody SupplierTypeRequestDTO dto) {
        return ResponseEntity.ok(service.createSupplierType(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierTypeResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSupplierTypeById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSupplierTypes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierTypeResponseDTO> update(@PathVariable Integer id,
                                                          @RequestBody SupplierTypeRequestDTO dto) {
        return ResponseEntity.ok(service.updateSupplierType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteSupplierType(id);
        return ResponseEntity.noContent().build();
    }
}
