package com.example.fieldsync_inventory_api.dto.stock.movement_type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.enums.EffectOnStock;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementTypeResponseDTO {
    private Integer id;
    private String name;
    private EffectOnStock effectOnStock;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
