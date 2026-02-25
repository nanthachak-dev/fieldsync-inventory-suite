package com.example.fieldsync_inventory_api.dto.inventory.seed_batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.inventory.rice_generation.RiceGenerationCompactDTO;
import com.example.fieldsync_inventory_api.dto.inventory.rice_variety.RiceVarietyCompactDTO;
import com.example.fieldsync_inventory_api.dto.inventory.season.SeasonCompactDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedBatchResponseDTO {
    private Long id;
    private RiceVarietyCompactDTO variety;
    private RiceGenerationCompactDTO generation;
    private SeasonCompactDTO season;
    private Integer year;
    private Boolean grading;
    private Boolean germination;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
