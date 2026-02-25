package com.example.fieldsync_inventory_api.dto.inventory.rice_generation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceGenerationCompactDTO {
    private Integer id;
    private String name;
    private String description;
}
