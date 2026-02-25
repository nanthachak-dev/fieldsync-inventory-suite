package com.example.fieldsync_inventory_api.dto.inventory.season;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
