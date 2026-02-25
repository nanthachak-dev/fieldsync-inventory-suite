package com.example.fieldsync_inventory_api.dto.inventory.rice_variety;

import lombok.*;

// For receiving data from client (create/update)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceVarietyRequestDTO {
    private String name;
    private String description;
    private String imageUrl;
}
