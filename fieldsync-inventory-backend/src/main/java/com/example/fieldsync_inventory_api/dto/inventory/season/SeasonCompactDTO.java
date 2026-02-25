package com.example.fieldsync_inventory_api.dto.inventory.season;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonCompactDTO {
    private Integer id;
    private String name;
    private String description;
}
