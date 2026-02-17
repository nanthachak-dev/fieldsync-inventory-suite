package com.example.fieldsync_inventory_backend.controller.procurement;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier.SupplierRequestDTO;
import com.example.fieldsync_inventory_backend.dto.procurement.supplier.SupplierResponseDTO;
import com.example.fieldsync_inventory_backend.service.procurement.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService service;

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> create(@RequestBody SupplierRequestDTO dto) {
        return ResponseEntity.ok(service.createSupplier(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSupplierById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSuppliers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> update(@PathVariable Integer id,
                                                      @RequestBody SupplierRequestDTO dto) {
        return ResponseEntity.ok(service.updateSupplier(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
