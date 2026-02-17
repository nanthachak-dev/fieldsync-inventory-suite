package com.example.fieldsync_inventory_backend.dto.inventory.rice_variety;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceVarietyCompactDTO {
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
}
