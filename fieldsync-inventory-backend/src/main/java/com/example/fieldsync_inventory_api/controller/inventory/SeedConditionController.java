package com.example.fieldsync_inventory_api.controller.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.seed_condition.SeedConditionRequestDTO;
import com.example.fieldsync_inventory_api.dto.inventory.seed_condition.SeedConditionResponseDTO;
import com.example.fieldsync_inventory_api.service.inventory.SeedConditionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seed-conditions")
@RequiredArgsConstructor
public class SeedConditionController {
    private final SeedConditionService service;

    @PostMapping
    public ResponseEntity<SeedConditionResponseDTO> create(@RequestBody SeedConditionRequestDTO dto) {
        return ResponseEntity.ok(service.createSeedCondition(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeedConditionResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSeedConditionById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeedConditionResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSeedConditions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeedConditionResponseDTO> update(@PathVariable Integer id,
                                                    @RequestBody SeedConditionRequestDTO dto) {
        return ResponseEntity.ok(service.updateSeedCondition(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteSeedCondition(id);
        return ResponseEntity.noContent().build();
    }
}