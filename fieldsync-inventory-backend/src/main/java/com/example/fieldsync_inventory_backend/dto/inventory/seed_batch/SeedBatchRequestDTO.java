package com.example.fieldsync_inventory_backend.dto.inventory.seed_batch;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedBatchRequestDTO {
    private Long id;
    private Integer varietyId;
    private Integer generationId;
    private Integer seasonId;
    private Integer year;
    private Boolean grading;
    private Boolean germination;
    private String description;
}
