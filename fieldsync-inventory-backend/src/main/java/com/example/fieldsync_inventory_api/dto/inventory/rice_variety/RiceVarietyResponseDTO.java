package com.example.fieldsync_inventory_api.dto.inventory.rice_variety;

import lombok.*;

import java.time.Instant;

// For sending data to client (read)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceVarietyResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
