package com.example.fieldsync_inventory_backend.controller.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchRequestDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.seed_batch.SeedBatchResponseDTO;
import com.example.fieldsync_inventory_backend.service.inventory.SeedBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seed-batches")
public class SeedBatchController {

    private final SeedBatchService seedBatchService;

    @GetMapping
    public List<SeedBatchResponseDTO> getAll() {
        return seedBatchService.getAllSeedBatches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeedBatchResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seedBatchService.getSeedBatchById(id));
    }

    @PostMapping
    public ResponseEntity<SeedBatchResponseDTO> create(@RequestBody SeedBatchRequestDTO dto) {
        return ResponseEntity.ok(seedBatchService.createSeedBatch(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeedBatchResponseDTO> update(@PathVariable Long id,
                                                       @RequestBody SeedBatchRequestDTO dto) {
        return ResponseEntity.ok(seedBatchService.updateSeedBatch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seedBatchService.deleteSeedBatch(id);
        return ResponseEntity.noContent().build();
    }
}