package com.example.fieldsync_inventory_api.controller.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.rice_generation.RiceGenerationRequestDTO;
import com.example.fieldsync_inventory_api.dto.inventory.rice_generation.RiceGenerationResponseDTO;
import com.example.fieldsync_inventory_api.service.inventory.RiceGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rice-generations")
@RequiredArgsConstructor
public class RiceGenerationController {

    private final RiceGenerationService service;

    @PostMapping
    public ResponseEntity<RiceGenerationResponseDTO> create(@RequestBody RiceGenerationRequestDTO dto) {
        return ResponseEntity.ok(service.createRiceGeneration(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiceGenerationResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getRiceGenerationById(id));
    }

    @GetMapping
    public ResponseEntity<List<RiceGenerationResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllRiceGenerations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RiceGenerationResponseDTO> update(@PathVariable Integer id, @RequestBody RiceGenerationRequestDTO dto) {
        return ResponseEntity.ok(service.updateRiceGeneration(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteRiceGeneration(id);
        return ResponseEntity.noContent().build();
    }
}
