package com.example.fieldsync_inventory_backend.dto.inventory.seed_condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedConditionRequestDTO {
    private String name;
    private String description;
}