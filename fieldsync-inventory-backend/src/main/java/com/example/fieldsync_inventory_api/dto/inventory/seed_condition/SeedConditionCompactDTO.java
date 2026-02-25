package com.example.fieldsync_inventory_api.dto.inventory.seed_condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedConditionCompactDTO {
    private Integer id;
    private String name;
    private String description;
}
