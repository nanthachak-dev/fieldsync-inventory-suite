package com.example.fieldsync_inventory_api.dto.inventory.season;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonRequestDTO {
    private String name;
    private String description;
}
