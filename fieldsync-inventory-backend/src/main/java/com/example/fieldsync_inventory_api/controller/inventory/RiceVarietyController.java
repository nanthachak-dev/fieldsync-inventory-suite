package com.example.fieldsync_inventory_api.controller.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.rice_variety.RiceVarietyRequestDTO;
import com.example.fieldsync_inventory_api.dto.inventory.rice_variety.RiceVarietyResponseDTO;
import com.example.fieldsync_inventory_api.service.inventory.RiceVarietyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rice-varieties")
@RequiredArgsConstructor
public class RiceVarietyController {
    private final RiceVarietyService service;

    @PostMapping
    public ResponseEntity<RiceVarietyResponseDTO> create(@RequestBody RiceVarietyRequestDTO dto) {
        return ResponseEntity.ok(service.createRiceVariety(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RiceVarietyResponseDTO> update(@PathVariable Integer id,
                                                         @RequestBody RiceVarietyRequestDTO dto) {
        return ResponseEntity.ok(service.updateRiceVariety(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteRiceVariety(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiceVarietyResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getRiceVarietyById(id));
    }

    @GetMapping
    public ResponseEntity<List<RiceVarietyResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllRiceVarieties());
    }
}
