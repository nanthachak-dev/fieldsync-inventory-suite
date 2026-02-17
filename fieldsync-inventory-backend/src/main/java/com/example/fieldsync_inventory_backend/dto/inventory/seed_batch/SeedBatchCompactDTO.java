package com.example.fieldsync_inventory_backend.dto.inventory.seed_batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.inventory.rice_generation.RiceGenerationCompactDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.rice_variety.RiceVarietyCompactDTO;
import com.example.fieldsync_inventory_backend.dto.inventory.season.SeasonCompactDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedBatchCompactDTO {
    private Long id;
    private RiceVarietyCompactDTO variety;
    private RiceGenerationCompactDTO generation;
    private SeasonCompactDTO season;
    private Integer year;
    private Boolean grading;
    private Boolean germination;
    private String description;
}
