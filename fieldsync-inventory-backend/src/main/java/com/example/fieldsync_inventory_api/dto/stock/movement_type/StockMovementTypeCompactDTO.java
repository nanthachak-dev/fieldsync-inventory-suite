package com.example.fieldsync_inventory_api.dto.stock.movement_type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.enums.EffectOnStock;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementTypeCompactDTO {
    private Integer id;
    private String name;
    private EffectOnStock effectOnStock;
    private String description;
}
