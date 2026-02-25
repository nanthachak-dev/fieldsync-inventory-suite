package com.example.fieldsync_inventory_api.dto.inventory.rice_generation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceGenerationResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}