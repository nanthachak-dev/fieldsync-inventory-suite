package com.example.fieldsync_inventory_api.controller.inventory;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.season.SeasonRequestDTO;
import com.example.fieldsync_inventory_api.dto.inventory.season.SeasonResponseDTO;
import com.example.fieldsync_inventory_api.service.inventory.SeasonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonService service;

    @PostMapping
    public ResponseEntity<SeasonResponseDTO> create(@RequestBody SeasonRequestDTO dto) {
        return ResponseEntity.ok(service.createSeason(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSeasonById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeasonResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSeasons());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeasonResponseDTO> update(@PathVariable Integer id,
                                                         @RequestBody SeasonRequestDTO dto) {
        return ResponseEntity.ok(service.updateSeason(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteSeason(id);
        return ResponseEntity.noContent().build();
    }
}
