package com.example.fieldsync_inventory_backend.dto.inventory.rice_generation;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceGenerationRequestDTO {
    private String name;
    private String description;
}